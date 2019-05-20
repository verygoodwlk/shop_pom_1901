package com.qf.shop_goods_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
@MapperScan("com.qf.dao")
@DubboComponentScan("com.qf.serviceimpl")
public class ShopGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsServiceApplication.class, args);
    }

}
