package com.example.demo.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_fruit")
//mybatisPlus 是不用你写sql语句的，所以需要跟他说你是要操作哪个表
//简化getter和setter
public class User {

    //必须告诉myp 表里的主键是什么
    //这里只是说数据库的fid对应的是这边的id，但是在postman测试的时候，传进来的对象（json格式），value必须是id，而不是fid，否则会找不到
    //这里在新增的时候很奇怪，还是保持名字相同为好
    @TableId(value = "fid",type= IdType.AUTO)
    private Integer fid;
    private String fname;
    private Integer price;
    private Integer fcount;
//    @TableField(value = "remark")指定数据库的字段名称
    private String remark;
    private String avatarUrl;
    @TableField(value = "createTime")
    private Date createTime;
}
