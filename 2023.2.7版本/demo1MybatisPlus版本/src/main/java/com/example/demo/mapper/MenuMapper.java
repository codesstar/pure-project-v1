package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Files;
import com.example.demo.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {


//    @Select("SELECT pid FROM sys_menu WHERE pid is not NULL and id in (${menuIds})")


//    List<Integer> selectFather(@Param("menuIds")List<Integer> menuIds);
}

