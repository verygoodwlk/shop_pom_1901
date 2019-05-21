package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/20 16:02
 */
@Controller
@RequestMapping("/res")
public class ResController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传图片
     * @return
     */
    @RequestMapping("/uploadImg")
    @ResponseBody
    public Map<String, String> uploadImg(MultipartFile file){

        //获得上传的图片名称
        String fname = file.getOriginalFilename();
        //获得上传的图片大小
        long flength = file.getSize();

        //截取后缀
        int index = fname.lastIndexOf(".");
        String houzui = fname.substring(index + 1);

        Map<String, String> map = new HashMap<>();

        //上传图片到FastDFS上
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(),
                    flength,
                    houzui,
                    null
            );

            map.put("code", "0000");
            map.put("filepath", storePath.getFullPath());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("code","0001");


//        //获得上传路径
//        String path = ResController.class.getResource("/").getPath() + "static/file";
//        try (
//                //上传的文件流
//                InputStream in = file.getInputStream();
//                //后缀 - 后缀就是告诉操作系统用什么软件打开这个文件
//                OutputStream out = new FileOutputStream(path + "/" + UUID.randomUUID().toString() + houzui);
//        ){
//            IOUtils.copy(in, out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return map;
    }


    /*@RequestMapping("/getImg")
    @ResponseBody
    public String getImg(String path){
        String filePath = "http://192.168.227.138/" + path;
        filePath = filePath.replace("/", "\\");
        return filePath;
    }*/
}
