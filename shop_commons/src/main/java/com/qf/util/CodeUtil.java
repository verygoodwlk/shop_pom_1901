package com.qf.util;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/4 10:34
 */
public class CodeUtil {

    /**
     * 创建验证码
     * @return
     */
    public static int createCode(){
        return (int)(Math.random() * 9000 + 1000);//[0 ~ 1)  1000~9999
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(CodeUtil.createCode());
        }
    }
}
