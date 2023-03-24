package com.example.demo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.RoleMenu;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleService extends ServiceImpl<RoleMapper,Role> {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;
    @Autowired
    private MenuService menuService;


    @Transactional

    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {

        //先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);

        //因为vue的数据选择有个bug，就是如果选择了子节点，那么它的父节点不会选上，但是这个实际上是不符合逻辑的
        //所以我们必须自己加上逻辑判断如果选中的子节点有父节点，那么就必须让其选中的加上父节点

//        menuMapper.selectFather(menuIds);



//        QueryWrapper<Menu> queryWrapper2 = new QueryWrapper<>();
//        queryWrapper2.isNotNull("pid");
//        queryWrapper2.in("id",menuIds);
//
//        List<Menu> listMenu= menuService.list(queryWrapper2);
//
//        Set<Integer> numberList=new HashSet<>();
//        for(int i=0;i<listMenu.size();i++){
//            numberList.add(listMenu.get(i).getPid());
//        }
//        for(Integer parentId:numberList){
//            if(!menuIds.contains(parentId))
//            {
//                menuIds.add(parentId);
//            }
//        }
        for(Integer menuId :menuIds){
            RoleMenu roleMenu= new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}

//select pid from sys_menu where id= menuId and pid!=null