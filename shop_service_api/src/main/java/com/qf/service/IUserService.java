package com.qf.service;

import com.qf.entity.User;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/4 11:08
 */
public interface IUserService {

    int register(User user);

    User login(User user);
}
