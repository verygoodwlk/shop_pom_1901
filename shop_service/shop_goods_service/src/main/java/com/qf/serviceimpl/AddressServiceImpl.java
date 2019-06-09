package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.AddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 9:24
 */
@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> queryByUid(Integer uid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);

        List list = addressMapper.selectList(queryWrapper);

        return list;
    }

    @Override
    public int insertAddress(Address address) {
//        if(address.getIsdefault() == 1){
//            //新增的收货地址为默认
//
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("uid", address.getUid());
//            queryWrapper.eq("isdefault", 1);
//            Address address1 = addressMapper.selectOne(queryWrapper);
//
//            if(address1 != null){
//                address1.setIsdefault(0);
//                addressMapper.updateById(address1);
//            }
//        }
//        return addressMapper.insert(address);

        return addressMapper.insertAddress(address);
    }

    @Override
    public Address queryById(Integer id) {
        return addressMapper.selectById(id);
    }
}
