package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/21 15:42
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    /**
     * 根据关键词进行搜索
     * @return
     */
    @RequestMapping("/searchByKeyWord")
    public String searchByKey(String keyword, Model model){

        //通过关键词进行搜索
        System.out.println("进行商品的搜索，关键词是：" + keyword);

        return "searchlist";
    }
}
