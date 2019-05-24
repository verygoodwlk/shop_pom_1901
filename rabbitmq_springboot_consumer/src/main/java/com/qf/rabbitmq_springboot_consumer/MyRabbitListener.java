package com.qf.rabbitmq_springboot_consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 14:45
 */
@Component
public class MyRabbitListener {

    @RabbitListener(queues = "sb_queue")
    public void myHandler(String msg){
        System.out.println("监听到RabbitMQ发送过来的消息：" + msg);
    }
}
