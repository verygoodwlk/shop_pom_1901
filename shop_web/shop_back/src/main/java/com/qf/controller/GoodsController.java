package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${img.server}")
    private String imgPath;

    /**
     * 后台商品管理
     * @return
     */
    @RequestMapping("/list")
    public String goodsList(Model model){

        //调用商品服务，查询商品列表
        List<Goods> goods = goodsService.queryList();
        model.addAttribute("goods", goods);

        model.addAttribute("imgPath", imgPath);

        return "goodslist";
    }

    /**
     * 添加商品
     * @return
     */
    @RequestMapping("/add")
    public String goodsAdd(Goods goods, String[] gimageList){

        //处理商品图片
        String gimages = "";
        for (String s : gimageList) {
            if(gimages.length() > 0){
                gimages += "|";
            }
            gimages += s;
        }
        goods.setGimages(gimages);

        //调用服务层，添加商品
        goodsService.addGoods(goods);

        return "redirect:/goods/list";
    }
}
