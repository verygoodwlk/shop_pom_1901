package com.qf.aop;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/5 14:28
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface IsLogin {

    boolean mustLogin() default false;
}
