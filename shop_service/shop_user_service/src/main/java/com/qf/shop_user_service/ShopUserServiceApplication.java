package com.qf.shop_user_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.dao")
@DubboComponentScan("com.qf.serviceimpl")
public class ShopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserServiceApplication.class, args);
    }

}
