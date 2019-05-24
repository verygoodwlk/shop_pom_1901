package com.qf.listener;

import com.qf.controller.ItemController;
import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 15:26
 */
@Component
public class RabbitMqListener {

    @Autowired
    private Configuration configuration;

    @RabbitListener(queues = "item_queue")
    public void goodsMsgHander(Goods goods){

        //静态页面输出的路径 - 输出的静态页面必须能够让外界访问
        String path = ItemController.class.getResource("/static/html/").getPath() + goods.getId() + ".html";

        try (
                Writer out = new FileWriter(path)
        ){
            //获得商品详情页模板
            Template template = configuration.getTemplate("goods.ftl");

            //获得商品的对应数据 - 调用该商品服务查询商品详细信息
            Map map = new HashMap();
            String[] gimages = goods.getGimages().split("\\|");
            map.put("goods", goods);
            map.put("gimages", gimages);

            //生成静态页
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
