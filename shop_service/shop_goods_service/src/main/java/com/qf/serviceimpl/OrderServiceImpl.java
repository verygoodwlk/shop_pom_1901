package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.OrderDetilsMapper;
import com.qf.dao.OrderMapper;
import com.qf.entity.*;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 11:43
 */
@Service
public class OrderServiceImpl implements IOrderService {


    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetilsMapper orderDetilsMapper;

    @Override
    @Transactional
    public Orders insertOrder(User user, Integer aid) {

        //通过aid查询收货地址的详细信息
        Address address = addressService.queryById(aid);

        //获得所有购物车的信息
        List<Cart> cartList = cartService.getCartList(null, user);

        //总价
        BigDecimal allprice = BigDecimal.valueOf(0);
        for (Cart cart : cartList) {
            allprice = allprice.add(cart.getXiaoji());
        }

        //创建订单对象
        Orders orders = new Orders(
                0,
                UUID.randomUUID().toString(),
                user.getId(),
                address.getPerson(),
                address.getAddress(),
                address.getPhone(),
                new Date(),
                allprice,
                0,
                null
        );

        //添加订单
        orderMapper.insert(orders);

        //添加订单详情
        for (Cart cart : cartList) {
            OrderDetils orderDetils = new OrderDetils(
                    0,
                    orders.getId(),
                    cart.getGid(),
                    cart.getGoods().getGname(),
                    cart.getGoods().getGprice(),
                    cart.getGnumber(),
                    cart.getGoods().getGimages(),
                    cart.getXiaoji()
            );

            orderDetilsMapper.insert(orderDetils);
        }

        //清空购物车
        cartService.deleteCartsByUid(user.getId());

        return orders;
    }

    @Override
    public List<Orders> queryAllOrders(Integer uid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        queryWrapper.orderByDesc("createtime");
        List<Orders> orders = orderMapper.selectList(queryWrapper);

        //根据订单列表查询订单详情
        for (Orders order : orders) {

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("oid", order.getId());
            List<OrderDetils> list = orderDetilsMapper.selectList(queryWrapper2);

            order.setOrderDetils(list);
        }

        return orders;
    }

    @Override
    public Orders queryById(Integer id) {
        return null;
    }

    @Override
    public Orders queryByOrderId(String orderid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderid", orderid);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateOrder(Orders orders) {
        return orderMapper.updateById(orders);
    }
}
