package com.qf.util;

import java.security.MessageDigest;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/4 11:14
 */
public class MD5Util {

    private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * MD5加密
     * @param content 字符
     * @return
     */
    public static String content2MD5(String content){
        String resultString = null;
        try{
            resultString = new String(content);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
        }catch (Exception e){
        }
        return resultString;
    }


    private static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

}
