package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;
    private String orderid;
    private int uid;
    private String person;
    private String address;
    private String phone;
    private Date createtime;
    private BigDecimal allprice;//总价
    private int status = 0;//待支付
}
