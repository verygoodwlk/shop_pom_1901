package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.GoodsMapper;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/20 14:53
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Reference
    private ISearchService searchService;

    @Override
    public List<Goods> queryList() {
        List<Goods> goods = goodsMapper.selectList(null);
        return goods;
    }

    @Override
    public int addGoods(Goods goods) {
        //保存商品到数据库中
        goodsMapper.insert(goods);
        //同步商品到索引库中
        searchService.addGoods(goods);
        return 1;
    }
}
