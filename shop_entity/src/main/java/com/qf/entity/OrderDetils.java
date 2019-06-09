package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 11:39
 */
@TableName("order_detils")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetils implements Serializable {

    private int id;
    private int oid;
    private int gid;
    private String gname;
    private BigDecimal gprice;
    private int gnumber;
    private String gimage;
    private BigDecimal xiaoji;
}
