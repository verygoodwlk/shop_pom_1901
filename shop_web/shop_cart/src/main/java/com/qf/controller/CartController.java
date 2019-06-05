package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/5 14:12
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    /**
     * 添加购物车
     * @return
     */
    @IsLogin
    @RequestMapping("/addCart")
    public String addCart(int gid, int gnumber, User user){
        System.out.println("添加购物车的商品：" + gid + " 购买数量为：" + gnumber);
        System.out.println("当前是否登录：" + user);
        return "succ";
    }
}
