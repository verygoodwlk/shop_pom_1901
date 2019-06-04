package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;
import java.io.File;
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

}
