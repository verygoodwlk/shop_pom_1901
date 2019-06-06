package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.aop.IsLogin;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/5 14:12
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加购物车
     * @return
     */
    @IsLogin
    @RequestMapping("/addCart")
    public String addCart(Cart cart,
                          User user,
                          @CookieValue(name = "cart_token", required = false) String cartToken,
                          HttpServletResponse response){
        System.out.println("添加购物车的商品：" + cart.getGid() + " 购买数量为：" + cart.getGnumber());
        System.out.println("当前是否登录：" + user);


        //根据商品id查询商品信息
        Goods goods = goodsService.queryById(cart.getGid());
        //设置购物车的商品价格
        cart.setGprice(goods.getGprice());
        cart.setXiaoji(goods.getGprice().multiply(BigDecimal.valueOf(cart.getGnumber())));
        cart.setCreatetime(new Date());

        if(user != null){
            //已经登录的状态 - 数据库
            //设置购物车所属用户
            cart.setUid(user.getId());

            //调用服务层保存数据库
            cartService.addCart(cart);

        } else {
            //未登录的状态 - redis

            //生成一个购物车的令牌
            cartToken = cartToken != null ? cartToken : UUID.randomUUID().toString();

            //将购物车的信息保存到redis中
            //集合 - 双向链表
            redisTemplate.opsForList().rightPush(cartToken, cart);
            redisTemplate.expire(cartToken, 365, TimeUnit.DAYS);

            //将cartToken写入浏览器cookie
            Cookie cookie = new Cookie("cart_token", cartToken);
            cookie.setMaxAge(1 * 60 * 60 * 24 * 365);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "succ";
    }


    /**
     * 前端获取购物车信息
     * @return
     */
    @IsLogin
    @RequestMapping("/list")
    @ResponseBody
    public String cartListAjax(
            @CookieValue(name = "cart_token", required = false) String cartToken,
            User user,
            String callback){

        //获取购物车列表
        List<Cart> cartList = cartService.getCartList(cartToken, user);
        String result = JSON.toJSONString(cartList);

        return callback != null ? callback + "(" + result + ")" : result;
    }


    /**
     * 展示购物车列表
     * @return
     */
    @IsLogin
    @RequestMapping("/showlist")
    public String showCartList(
            @CookieValue(name = "cart_token", required = false) String cartToken,
            User user,
            Model model){

        //获得购物车列表
        List<Cart> cartList = cartService.getCartList(cartToken, user);
        model.addAttribute("cartList", cartList);

        //总价
        BigDecimal allprice = BigDecimal.valueOf(0);
        for (Cart cart : cartList) {
            allprice = allprice.add(cart.getXiaoji());
        }
        model.addAttribute("allprice", allprice.doubleValue());

        return "cartlist";
    }
}


