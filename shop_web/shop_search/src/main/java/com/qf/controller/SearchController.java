package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/21 15:42
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    /**
     * 根据关键词进行搜索
     * @return
     */
    @RequestMapping("/searchByKeyWord")
    public String searchByKey(String keyword, Model model){

        //通过关键词进行搜索
        System.out.println("进行商品的搜索，关键词是：" + keyword);
        List<Goods> goods = searchService.queryByKeyWord(keyword);
        model.addAttribute("goods", goods);

        return "searchlist";
    }
}
