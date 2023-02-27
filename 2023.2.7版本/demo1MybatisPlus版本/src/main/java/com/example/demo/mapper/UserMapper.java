package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//@Mapper
public interface UserMapper extends BaseMapper<User> {
//
//    @Select("select * from t_fruit")
//    List<User> findAll();
//
//    @Insert("INSERT into t_fruit(fid,fname,price,fcount,remark) VALUES (#{fid},#{fname},#{price},#{fcount},#{remark})")
//    int insert(User user);
//
//    @Update("update t_fruit set fname=#{fname},price=#{price},fcount=#{fcount},remark=#{remark} where fid=#{fid}")
//    int update(User user);
//
//    @Delete("delete from t_fruit where fid =#{fid}")
//    Integer deleteById(Integer id);
//
////    @Select("select * from t_fruit where fname like #{fname} limit #{pageNum},#{pageSize}")
////    List<User> selectPage(Integer pageNum, Integer pageSize,String fname);
//
//    @Select("select count(*) from t_fruit where fname like #{fname}")
//    Integer selectTotal(String fname);


}
