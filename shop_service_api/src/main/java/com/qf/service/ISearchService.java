package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {

    List<Goods> queryByKeyWord(String keyword);

    int addGoods(Goods goods);
}
