package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.Orders;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public Orders insert(Integer aid, User user){
        //添加订单信息
        Orders orders = orderService.insertOrder(user, aid);
        return orders;
    }

    /**
     * 查询用户的订单列表
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/list")
    public String list(User user, Model model){

        //调用服务查询当前用户的订单列表
        List<Orders> orders = orderService.queryAllOrders(user.getId());
        model.addAttribute("orders", orders);

        return "orderlist";
    }

    /**
     * 调用支付宝进行支付
     * @return
     */
    @RequestMapping("/pay")
    public void pay(String orderid, HttpServletResponse response){

        Orders orders = orderService.queryByOrderId(orderid);

        AlipayClient alipayClient =
                new DefaultAlipayClient(
                        "https://openapi.alipaydev.com/gateway.do",//支付宝网关 - 固定写法
                        "2016073000127352",//APPID - 创建应用后生成
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMw4pMpBLiA6/VfdmpteRXqLejjIv53LiH8iifXuDYFJjDGdjTaGKAPdo/bUAolvbEm88rQXBv0P220KW9C+s/naX+lR2YVtGYPM86ZcgyVs82lXeEKBHQVa38ECljFV1xG8NcNf58mAY3XG0E03y9LQ5F3m6vCbSOdE7vFw7aDIQk1QDpgielJhxbQlz4ALgXQPLqdhb7aco9VJ+7OdO/1lGqhTWlCbzoQcEOxRKeRqQQG4HN7WJMco3Yzu2AIOs0/KjrEyBJWRttmDSWrcVdg/cs6QBxzduMj19WLQ3Izk3Efjc8oO8gFLB1nawq+3vhMwMhFd8Cmfb+GTo8jH2lAgMBAAECggEAYL1CHsnj8FB4V1DWdjv7YgMhsdS1nlKCv2s9X8nk3JvLIMpyqVfj2h6oD08V+M9HaHdsOV4P18jZlPpv7Sxz0JSXsA7q9/cytF2WTZwFqhLe9bRjHwAqvfMkT0cMKnxUa5eY+5Zf/2C7ZPCwO9DcuWZtmLb1enZBEGX+mXJPUqqbV4nsHPpOpHDUoB3rcim+dRx4cuGWiLo1zdwq4PWgeSWgDhH5fOiWrWvU43d4kKbsh43Wp4+qzUK2U9z+/BCA7yfs3G1W25VDEI9ES+vSmmYWO/HJ1a3O3ZzsTEdlVvP7vGXiVA/WGVm/jDRK9EdJKOkhrZ4RM5NE6BGSIhbnQQKBgQDvtvA/UB3TO+xIklUaCAg+klLsjAPYYtmU9OUua96cDQvNUezBqIbBSsf2aLtAG2XLg8opAhI62GjkwvJeDKBxoTe3s77WLnWAVvzzv24wk7P47Nlo1PQI67M/fiqYnD4HMTzroLu5heRxfgxgewTpweKCO1j9jSydNhif6ddZSwKBgQCWU6wrgbXt1hXx4Y61KMeLcPOWD1x0vQbN1jOk+yRH5Jeg4XNln71Yy8AqxxIe2xgnfm+MsXJ3lxB18F/0WDjrzTGjzN6u/8+D9MNUsYHLf+suWMBZdRDoTGY1QO9Zz2L4uis0LjUuF8eFmD05odrB3Y0CoeWhEBfRTgNdMI2ezwKBgACSEuhTrH6AOgPCNWmmcz6S4peljazKL3JnUtRVdWYxscF/XGzplciIAQnZHfj9F7TwT17hnmF9emJxBFqs9QTQOW26g83GtojjrB54ai67a0ZAahQRzbLnvLl992P9mzgxKc/1xfyfEPgkMKtaH5ze1UcyFPglieGl61EWIWkZAoGATG2uE1CXVpyJ+7hZ/fEFwvdiYODeeNvCIdTN3Qtynrqx/LGjIf/dHNA6m4UqyVVLKok13ao6MGM1k0DrCCfyiLCClG0MqpyUvNoslVrKStR2ldG5ucGzHuf47wpcpqSyBRNh892qXAzq/Bkyx6JqvjYOA1Cclw6/jULEoxSUbDECgYEAqEUg7/ideOdXZY+wQuLUc3JxJUMXWnGX3l0L0a/hv8ZD4cLtyehAr5V4gKKgDRk18t7Q8jVBj68Ns8MLrE+VQG+r3F6TaYi3zF5Pj0qpFSZstqoXE3f25S/4MK3MKGbM77UIASfLi/IiZJSsseZY6aSAGvI+FDs891mJwcacGHw=",//开发者私钥 -
                        "json",//参数支持的格式 - 固定json
                        "UTF-8",//编码集 GBK/UTF-8
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx+n+pEqTVi27mtj3CuUQXi2ixqeeTwE/0tGrW+sg6xtfajvJV67GYf2zzNxxBV0TYfhdbi70VI3DftEijg7GSNKoOilAu2DKQFqidnSxmN1Es1oRTaiaehqm1Uzs2uswpzBVR21iygLHujwthC8kNkMgxVFkjbE/qTn7z5wlsailtg6wF+hY3BcDCiaLyVLjEDngmrLyLXPLenjAuvXq20h9zV7CW9HXuhpPBDfsn4fv5TjgEl1smjJNr4O/VxICKDNPsvrCyNXhfroK9PEFFwH+4IWGeBUJAP2cSufNU0OA+UH+2xQnaR8Cz30QIgIslckBGuXQZvxqaY2mMMz14QIDAQAB",//支付宝公钥 - 上传商户公钥后，支付宝返回
                        "RSA2"); //获得初始化的AlipayClient

        //创建一个请求，这个请求是用来申请交易支付页面的
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        //设置同步返回请求 - 支付完成后，跳转到哪个请求？不能根据这个请求判断是否支付成功
        alipayRequest.setReturnUrl("http://localhost:8081");
        //设置异步返回请求 - 支付完成后，支付的结果通知到哪个url上
        alipayRequest.setNotifyUrl("http://verygoodwlk.xicp.net/order/payComplate");//在公共参数中设置回跳和通知地址
        //请求体的内容
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orders.getOrderid() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + orders.getAllprice().doubleValue() + "," +
                "    \"subject\":\"" + orders.getOrderid() + "\"" +
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付完成的异步通知接口
     */
    @RequestMapping("/payComplate")
    @ResponseBody
    public void payComplate(String out_trade_no, String trade_status){

        //TODO 验证请求是否来着支付宝

        if(trade_status.equals("TRADE_SUCCESS")){
            //交易成功
            Orders orders = orderService.queryByOrderId(out_trade_no);
            orders.setStatus(1);
            orderService.updateOrder(orders);
        }
    }
}
