package com.qf.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/20 16:02
 */
@Controller
@RequestMapping("/res")
public class ResController {

    /**
     * 上传图片
     * @return
     */
    @RequestMapping("/uploadImg")
    @ResponseBody
    public String uploadImg(MultipartFile file){

        //获得上传的图片名称
        String fname = file.getOriginalFilename();
        //获得上传的图片大小
        long flength = file.getSize();

        //截取后缀
        int index = fname.lastIndexOf(".");
        String houzui = fname.substring(index);


        //获得上传路径
        String path = ResController.class.getResource("/").getPath() + "static/file";
        try (
                //上传的文件流
                InputStream in = file.getInputStream();
                //后缀 - 后缀就是告诉操作系统用什么软件打开这个文件
                OutputStream out = new FileOutputStream(path + "/" + UUID.randomUUID().toString() + houzui);
        ){
            IOUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
