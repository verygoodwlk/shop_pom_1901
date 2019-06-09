package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Address;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 9:25
 */
public interface AddressMapper extends BaseMapper<Address> {

    int insertAddress(Address address);
}
