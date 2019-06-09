package com.qf.service;

import com.qf.entity.Cart;
import com.qf.entity.User;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/6 9:11
 */
public interface ICartService {

    int addCart(Cart cart);

    List<Cart> getCartList(String cartToken, User user);

    int syncCarts(String cartToken, User user);

    int deleteCartsByUid(Integer uid);
}
