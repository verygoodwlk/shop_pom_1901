package com.qf.rabbitmq_springboot_provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringbootProviderApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
        //给队列发送消息
        rabbitTemplate.convertAndSend("sb_queue", "来自SB的消息！");
    }

}
