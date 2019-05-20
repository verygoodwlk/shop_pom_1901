package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/20 14:31
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    /**
     * 后台商品管理
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(Model model){

        //调用商品服务，查询商品列表
        List<Goods> goods = goodsService.queryList();
        model.addAttribute("goods", goods);

        return "goodslist";
    }
}
