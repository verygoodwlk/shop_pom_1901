package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.util.Base64Util;
import com.qf.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/4 9:15
 */
@Controller
@RequestMapping("/sso")
public class SSOController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Reference
    private IUserService userService;

    @RequestMapping("/tologin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/toregister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/{page}")
    public String toPage(@PathVariable("page") String page){
        return page;
    }

    /**
     * 注册账号
     * @return
     */
    @RequestMapping("/register")
    public String register(User user, String code, Model model){

        //校验验证码
        String sCode = (String) redisTemplate.opsForValue().get(user.getEmail() + "_code");
        if(sCode == null || !sCode.equals(code)){
            //验证码不正确
            model.addAttribute("error", 1);
            return "register";
        }

        //验证码通过
        //删除验证码
        redisTemplate.delete(user.getEmail() + "_code");

        //通过dubbo调用服务器进行注册
        int result = userService.register(user);
        if(result == -1){
            //用户名已经存在
            model.addAttribute("error", 2);
            return "register";
        } else if(result == -2){
            //邮箱已经存在
            model.addAttribute("error", 3);
            return "register";
        }

        //注册成功
        return "redirect:/sso/tologin";
    }

    /**
     * 登录账号
     * @return
     */
    @RequestMapping("/login")
    public String login(User user, String returnUrl, Model mode, HttpServletResponse response){

        if(returnUrl == null){
            returnUrl = "http://localhost:8081";
        }

        //调用服务进行登录认证
        User loginUser = userService.login(user);
        if(loginUser == null){
            //账号或者密码错误
            mode.addAttribute("error", "账号或者密码错误！");
            return "login";
        }

        //登录成功了
        String loginToken = UUID.randomUUID().toString();
        //token作为key，用户信息作为value 放入redis中
        redisTemplate.opsForValue().set(loginToken, loginUser);
        redisTemplate.expire(loginToken, 7, TimeUnit.DAYS);

        //放入用户浏览器的cookie中
        Cookie cookie = new Cookie("login_token", loginToken);
        cookie.setMaxAge(1 * 60 * 60 * 24 * 7);//最大超时时间
        cookie.setPath("/");//所有请求都会携带该cookie
//        cookie.setDomain(".sb.com");//所有二级域名共享cookie， cookie不能一级域名共享
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(cookie);

        return "redirect:" + returnUrl;
    }


    /**
     * 认证当前客户端是否登录
     *
     * cookie - login_token - uuid
     *
     * uuid - redis - user
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/islogin")
//    @CrossOrigin
    public String isLogin(
            @CookieValue(name="login_token", required=false)
                    String loginToken,
            @RequestParam(value = "loginToken", required = false)
                    String loginToken2,
            String callback){

        loginToken = loginToken != null ? loginToken : loginToken2;

        //开始认证当前用户是否登录
        String result = null;
        if(loginToken != null){
            //说明当前有cookie
            User user = (User) redisTemplate.opsForValue().get(loginToken);
            if(user != null){
                //说明认证成功
                result = JSON.toJSONString(user);
            }
        }

        return callback != null ? callback + "(" + result + ")" : result;
    }

    @RequestMapping("/logout")
    public String logout(
            @CookieValue(name="login_token", required=false) String loginToken,
            HttpServletResponse response){

        if(loginToken != null){
            //通过cookie 删除redis
            redisTemplate.delete(loginToken);

            //删除cookie
            Cookie cookie = new Cookie("login_token", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/sso/tologin";
    }

    /**
     * 跳转到找回密码的页面
     * @return
     */
    @RequestMapping("/toUpdatePassword")
    public String toUpdatePassword(String username, String token, Model model){

        //验证请求的有效性
        username = Base64Util.decodingBase64(username);

        model.addAttribute("username", username);
        model.addAttribute("token", token);

        return "updatePassword";
    }

    /**
     * 修改密码
     * @param username
     * @param password
     * @param token
     * @return
     */
    @RequestMapping("/updatePassword")
    public String updatePassword(String username, String password, String token, Model model){

        //校验修改密码的有效性
        String myToken = (String) redisTemplate.opsForValue().get(username + "_forget_token");

        if(myToken != null && myToken.equals(token)){
            //校验通过,调用服务修改密码
            userService.updatePassword(username, password);

            redisTemplate.delete(username + "_forget_token");

            return "succ";
        }

        //校验未通过
        model.addAttribute("error", "是一个非法或者过期的请求！");
        return "error";
    }

    /**
     * 发送邮件
     * @return
     */
    @RequestMapping("/sendMail")
    @ResponseBody
    public String sendMail(String email){

        //生成验证码
        int code = CodeUtil.createCode();

        //将code保存到redis中
        redisTemplate.opsForValue().set(email + "_code", code + "");
        redisTemplate.expire(email + "_code", 5, TimeUnit.MINUTES);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            //创建一个邮件帮助类，第二个参数为true表示支持附件
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            //设置标题
            messageHelper.setSubject("暴雪官方注册邮件");

            //设置发送者 - 公司的官方邮箱（我的新浪）
            messageHelper.setFrom("verygoodwlk@sina.cn");

            //设置接收者
            messageHelper.setTo(email);

            //设置邮箱内容
            messageHelper.setText("<a href='www.baidu.com'>验证码</a>为: " + code, true);

            //设置附件
            messageHelper.addAttachment("附件.jpg", new File("C:\\Users\\ken\\Pictures\\Saved Pictures\\奥格瑞玛.jpg"));

            //发送邮件
            javaMailSender.send(mimeMessage);

            return "succ";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }


    /**
     * 发送忘记密码的邮件
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendForgetMail")
    public Map<String, Object> sendForgetMail(String username){

        Map<String, Object> map = new HashMap<>();

        //用过usernama查询用户信息
        User user = userService.queryByUserName(username);
        if(user == null){
            //说明账号不存在
            map.put("code", -1);
            return map;
        }

        //说明账号存在
        String email = user.getEmail();//1120673996@qq.com 112*****@qq.com
        int index = email.indexOf("@");
        String str = email.substring(4, index);
        String emailStr = email.replace(str, "******");
        //给email发送找回密码的邮件（是一个url，点击这个url可以跳转到修改密码的页面）
        //时效性、一次性、独立性
        //http://localhost:8084/sso/toUpdatePassword?username=zhangsan


        String toMail = "http://mail." + email.substring(index + 1);

        //创建一封邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            //创建一个邮件帮助类，第二个参数为true表示支持附件
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            //设置标题
            messageHelper.setSubject("暴雪官方找回密码邮件");

            //设置发送者 - 公司的官方邮箱（我的新浪）
            messageHelper.setFrom("verygoodwlk@sina.cn");

            //设置接收者
            messageHelper.setTo(email);

            //设置邮箱内容
            String uuid = UUID.randomUUID().toString();//硬件（网卡、主板、mac...） + 当前时间 + ...
            redisTemplate.opsForValue().set(username + "_forget_token", uuid);
            redisTemplate.expire(username + "_forget_token", 5, TimeUnit.MINUTES);

            String url = "http://localhost:8084/sso/toUpdatePassword?username=" + Base64Util.encodingBase64(user.getUsername()) + "&token=" + uuid;
            messageHelper.setText("亲爱的用户，需要修改密码请点击<a href='" + url + "'>这里</a>", true);
            //发送邮件
            javaMailSender.send(mimeMessage);

            //发送成功
            map.put("code", 0);
            map.put("emailStr", emailStr);
            map.put("tomail", toMail);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("code", -2);
        return map;
    }
}
