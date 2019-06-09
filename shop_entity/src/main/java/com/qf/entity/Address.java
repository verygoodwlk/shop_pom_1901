package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version 1.0
 * @user ken
 * @date 2019/6/9 9:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "addresses")
public class Address implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;
    private String person;
    private String address;
    private String phone;
    private int uid;
    private int isdefault;

}
