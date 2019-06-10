package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 11:41
 */
public interface IOrderService {

    Orders insertOrder(User user, Integer aid);

    List<Orders> queryAllOrders(Integer uid);

    Orders queryById(Integer id);

    Orders queryByOrderId(String orderid);

    int updateOrder(Orders orders);
}
