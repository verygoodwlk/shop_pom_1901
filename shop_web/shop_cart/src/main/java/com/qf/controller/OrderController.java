package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 9:08
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private ICartService cartService;

    @Reference
    private IAddressService addressService;
    
    @Reference
    private IOrderService orderService;

    /**
     * 跳转到订单编辑页面
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/edit")
    public String toEditOrder(User user, Model model){

        //获得需要下单的购物车信息
        List<Cart> cartList = cartService.getCartList(null, user);
        model.addAttribute("carts", cartList);

        //获取当前用户的收货地址信息
        List<Address> addresses = addressService.queryByUid(user.getId());
        model.addAttribute("addresses", addresses);


        //计算订单的总价格
        BigDecimal allprice = BigDecimal.valueOf(0);
        for (Cart cart : cartList) {
            allprice = cart.getXiaoji().add(allprice);
        }
        model.addAttribute("allprice", allprice.doubleValue());

        return "orderedit";
    }


    /**
     * 添加订单
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/insert")
    @ResponseBody
    public String insert(Integer aid, User user){
        //添加订单信息
        int result = orderService.insertOrder(user, aid);
        return "succ";
    }
}
