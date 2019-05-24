package com.qf.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 10:29
 */
public class ConnectionUtil {

    private static ConnectionFactory connectionFactory;

    static{
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.227.138");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/admin");
        connectionFactory.setPort(5672);
    }

    public static Connection getConnection(){
        //获得rabbitmq的连接
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
