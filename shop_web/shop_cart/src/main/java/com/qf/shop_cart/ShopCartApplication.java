package com.qf.shop_cart;

import com.qf.aop.LoginAOP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.qf")
public class ShopCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCartApplication.class, args);
    }

    @Bean
    public LoginAOP getLoginAop(){
        return new LoginAOP();
    }
}
