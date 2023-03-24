package com.example.demo.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.mapper.DictMapper;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    @Resource
    private MenuMapper menuMapper;

    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper =new QueryWrapper<>();
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.like("name",name);
        List<Menu> list= list(queryWrapper);
        //找到pid为null的一级菜单
        List<Menu>parentNode=list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for(Menu menu:parentNode){
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNode;
    }

    public Set<Integer> demo() {
        List<Integer>list=new ArrayList<>();
        list.add(570);
        list.add(571);
        list.add(572);

        QueryWrapper<Menu> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.isNotNull("pid");
        queryWrapper2.in("id",list);

        List<Menu> listMenu= this.list(queryWrapper2);

        Set<Integer> numberList=new HashSet<>();
        for(int i=0;i<listMenu.size();i++){
            numberList.add(listMenu.get(i).getPid());
        }

        return  numberList;

    }
}