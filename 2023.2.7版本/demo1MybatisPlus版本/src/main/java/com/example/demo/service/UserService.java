package com.example.demo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper,User> {

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;


    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            String token = TokenUtils.genToken(one.getFid().toString(), one.getRemark());
            userDTO.setToken(token);

            String role = one.getRole();
            //注册用户时默认为普通用户
            if(StrUtil.isBlank(role))
            {
                role="ROLE_ADMIN";
            }


            //设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        } else {
            throw new ServiceException(Constants.Code_600, "用户名或密码错误");
        }
    }

    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);
        } else {
            throw new ServiceException(Constants.Code_600, "用户已存在");
        }
        return one;
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fname", userDTO.getFname());


        queryWrapper.eq("remark", userDTO.getRemark());
        //其实queryWrapper和getOne的底层逻辑就是封装了这句sql：SELECT fid,fname,price,fcount,remark FROM t_fruit WHERE (fname = ? AND remark = ?)
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.Code_500, "系统错误");
        }
        return one;
    }

    //
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);

        //但是如果选择了子菜单，那么子菜单的父菜单前台不会给你返回，需要自己写逻辑
        //menuIds是 是所有菜单,不管你是parent还是children，反正返回的是id
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        QueryWrapper<Menu> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.isNotNull("pid");
        queryWrapper2.in("id",menuIds);

        List<Menu> listMenu= menuService.list(queryWrapper2);

        Set<Integer> numberList=new HashSet<>();
        for(int i=0;i<listMenu.size();i++){
            numberList.add(listMenu.get(i).getPid());
        }
        for(Integer parentId:numberList){
            if(!menuIds.contains(parentId))
            {
                menuIds.add(parentId);
            }
        }


        //查出系统所有parent菜单，menus是所有父母菜单，因为菜单这种entity就都是parent级的
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选用户具有哪几个parent菜单，roleMenus里的就是当前该角色拥有的parent菜单
        for (Menu menu : menus) {
            //menu代表的是父母菜单

//            将父母菜单的所有子菜单查出来
            List<Menu> children =menu.getChildren();
            //把用户没有选择的剔除掉，menuIds包含用户选择的子菜单的id
            children.removeIf(child ->!menuIds.contains(child.getId()));
            menu.setChildren(children);

            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu); //568 569
            }

        }
        return roleMenus;
    }
}