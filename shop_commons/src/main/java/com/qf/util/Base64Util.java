package com.qf.util;

import java.util.Base64;

/**
 * Base64 - 编码
 * 将任何二进制数据 -> 字符串
 * 字符串 -> 二进制数据
 *
 * @version 1.0
 * @user ken
 * @date 2019/6/4 14:40
 */
public class Base64Util {

    /**
     * 编码字符串
     * @param content
     * @return
     */
    public static String encodingBase64(String content){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(content.getBytes());
    }

    public static String decodingBase64(String content){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(content);
        return new String(decode);
    }

    public static void main(String[] args) {
//        String str = "Hello World!";
//        String s = encodingBase64(str);
//        System.out.println(s);

//        String s = decodingBase64("SGVsbG8gV29ybGQh");
//        System.out.println(s);
    }
}
