package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/4 11:09
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {

        //判断用户名是否已经注册
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        User u = userMapper.selectOne(queryWrapper);
        if(u != null){
            //用户名已经被注册
            return -1;
        }

        //判断邮箱是否已经注册
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("email", user.getEmail());
        User u2 = userMapper.selectOne(queryWrapper2);
        if(u2 != null){
            //邮箱已经被注册
            return -2;
        }

        //可以正常注册
        //对密码进行加密
        user.setPassword(MD5Util.content2MD5(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public User login(User user) {
        return null;
    }
}
