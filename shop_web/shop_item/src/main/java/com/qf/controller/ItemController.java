package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/23 10:29
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private Configuration configuration;

    @Reference
    private IGoodsService goodsService;

    /**
     * 什么时候调用这个方法生成静态页？ - 添加商品时生成
     * 将指定的商品详情生成一个静态页面
     * @return
     */
    @ResponseBody
    @RequestMapping("/createHtml")
    public String createHtml(Integer gid){

        //静态页面输出的路径 - 输出的静态页面必须能够让外界访问
        String path = ItemController.class.getResource("/static/html/").getPath() + gid + ".html";

        try (
                Writer out = new FileWriter(path)
                ){
            //获得商品详情页模板
            Template template = configuration.getTemplate("goods.ftl");

            //获得商品的对应数据 - 调用该商品服务查询商品详细信息
            Goods goods = goodsService.queryById(gid);
            Map map = new HashMap();
            String[] gimages = goods.getGimages().split("\\|");
            map.put("goods", goods);
            map.put("gimages", gimages);

            //生成静态页
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "succ";
    }
}
