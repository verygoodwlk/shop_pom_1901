package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/6 9:06
 */
@TableName("shop_carts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {

    private int id;
    private int gid;
    private int uid;
    private int gnumber;
    private BigDecimal xiaoji;
    private Date createtime;
    private BigDecimal gprice;

    @TableField(exist = false)
    private Goods goods;
}
