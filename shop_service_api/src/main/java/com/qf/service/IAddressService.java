package com.qf.service;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> queryByUid(Integer uid);

    int insertAddress(Address address);

    Address queryById(Integer id);
}
