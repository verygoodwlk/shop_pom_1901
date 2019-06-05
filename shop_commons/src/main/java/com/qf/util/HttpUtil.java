package com.qf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/23 11:15
 */
public class HttpUtil {

    /**
     * 模拟浏览器发送一个Get请求
     * @return
     */
    public static String sendGet(String urlStr){

        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            URL url = new URL(urlStr);
            //打开一个连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //进行连接的设置
            conn.setRequestMethod("GET");//设置当前请求是一个GET请求
            conn.setConnectTimeout(1000 * 5);//连接的超时时间
            conn.setReadTimeout(1000 * 5);//读超时

            //开始连接服务器 - 模拟浏览器发送请求给指定的url
            conn.connect();

            //获得服务器的响应
            in = conn.getInputStream();
            String result = null;

            byte[] buffer = new byte[1024 * 10];
            int len = 0;
            //准备一个内存输出流
            out = new ByteArrayOutputStream();

            while((len = in.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }

            //将内存流的中的数据转换成String字符串
            byte[] bytes = out.toByteArray();

            if(bytes.length > 0){
                return new String(bytes);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String s = sendGet("http://localhost:8084/sso/islogin?loginToken=24baab0c-b318-4cbb-9554-feffe4d9525d");
        System.out.println("->" + s);
    }
}
