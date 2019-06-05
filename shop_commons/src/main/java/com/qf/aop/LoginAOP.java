package com.qf.aop;

import com.alibaba.fastjson.JSON;
import com.qf.entity.User;
import com.qf.util.HttpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * 前置增强
 * 后置增强
 * 环绕增强
 * 后置完成增强
 * 异常增强
 *
 * @version 1.0
 * @user ken
 * @date 2019/6/5 15:24
 */
@Aspect
@Component
public class LoginAOP {
    /**
     * 环绕增强
     * @return
     */
    @Around("execution(* *.*.controller.*.*(..)) && @annotation(IsLogin)")
    public Object isLogin(ProceedingJoinPoint joinPoint){

        //-------------------判断是否登录-----------------------
        //1、获得浏览器发送的cookie
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String loginToken = null;

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("login_token")){
                    loginToken = cookie.getValue();
                    break;
                }
            }
        }

        //登录的用户信息对象
        User user = null;

        if(loginToken != null){
            //有可能登录
            //2、调用sso工程判断是否登录
            String userJson = HttpUtil.sendGet("http://localhost:8084/sso/islogin?loginToken=" + loginToken);
            if(userJson != null){
                //登录成功
                user = JSON.parseObject(userJson, User.class);
            }
        }


        if(user == null){
            //说明当前浏览器并未登录
            //如果未登录判断IsLogin注解的mustLogin返回值是否为

            //通过环绕增强的参数对象获得目标的签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            //通过签名获得目标方法的反射对象
            Method method = signature.getMethod();

            //通过反射对象获得方法上的注解
            IsLogin isLogin = method.getAnnotation(IsLogin.class);

            //判断这个注解的方法返回值
            if(isLogin.mustLogin()){
                //说明当前需要强制登录

                //获得当前请求的路径（这个路径不会携带？后面的参数）
                String path = request.getRequestURL().toString();
                //获得请求?后面的参数列表
                String params = request.getQueryString();
                //最终的回跳路径
                String returnUrl = path + "?" + params;

                try {
                    returnUrl = URLEncoder.encode(returnUrl, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return "redirect:http://localhost:8084/sso/tologin?returnUrl=" + returnUrl;
            }
        }

        //修改目标方法形参列表

        //获得目标方法的所有参数列表
        Object[] args = joinPoint.getArgs();
        if(args != null){
            //以此循环所有参数对象
            for (int i = 0; i < args.length; i++) {
                //如果一个参数的类型是User类型
                if(args[i].getClass() == User.class){
                    //修改这个位置的参数
                    args[i] = user;
                }
            }
        }


        //-------------------调用目标方法，并且根据登录状态修改形参列表--------------------------
        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }

}
