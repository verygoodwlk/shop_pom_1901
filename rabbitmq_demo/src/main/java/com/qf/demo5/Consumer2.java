package com.qf.demo5;

import com.qf.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 14:12
 */
public class Consumer2 {

    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.basicConsume("myqueue2", true, new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                System.out.println(routingKey + "myqueue2接收到消息：" + new String(body, "utf-8"));
            }
        });
    }
}
