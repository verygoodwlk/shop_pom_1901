package com.qf.demo5;

import com.qf.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 14:03
 */
public class Provider {

    public static void main(String[] args) throws IOException {
        //1、连接RabbitMQ
        Connection connection = ConnectionUtil.getConnection();

        //2、获得通道
        Channel channel = connection.createChannel();

        //3、声明交换机
        channel.exchangeDeclare("my_topic_exchange", "topic");

        //4、声明队列
        channel.queueDeclare("myqueue1", false, false, false, null);
        channel.queueDeclare("myqueue2", false, false, false, null);

        //5、绑定队列和交换机
        channel.queueBind("myqueue1", "my_topic_exchange", "a.*");

        channel.queueBind("myqueue2", "my_topic_exchange", "a.#");

        //6、发布消息
        channel.basicPublish("my_topic_exchange", "a.b.c", null, "Hello World!".getBytes("utf-8"));

        connection.close();
    }
}
