package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/20 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;
    private String gname;
    private String ginfo;
    private BigDecimal gprice;
    private String gimages;
    private int tid = 1;
    private int gsave = 0;


//    public static void main(String[] args) {
//        System.out.println(5.0-4.9);
//
//        BigDecimal bigDecimal1 = BigDecimal.valueOf(5.0);
//        BigDecimal bigDecimal2 = BigDecimal.valueOf(4.9);
//        BigDecimal subtract = bigDecimal1.subtract(bigDecimal2);
//        System.out.println("结果：" + subtract.doubleValue());
//    }
}
