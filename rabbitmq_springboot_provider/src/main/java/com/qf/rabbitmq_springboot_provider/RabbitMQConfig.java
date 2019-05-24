package com.qf.rabbitmq_springboot_provider;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 14:36
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue getQueue(){
        return new Queue("sb_queue");
    }
}
