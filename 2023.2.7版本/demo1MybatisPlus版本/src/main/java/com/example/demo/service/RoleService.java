package com.example.demo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper,User> {

    private static final Log LOG=Log.get();


    public boolean saveUser(User user){
        return saveOrUpdate(user);
    }

    public UserDTO login(UserDTO userDTO) {
        User one=getUserInfo(userDTO);
            if(one!=null){
                BeanUtil.copyProperties(one,userDTO,true);
                String token = TokenUtils.genToken(one.getFid().toString(),one.getRemark());
                userDTO.setToken(token);
                return userDTO;UserService
            }else {
                throw new ServiceException(Constants.Code_600,"用户名或密码错误");
            }
    }

    public User register(UserDTO userDTO) {
        User one=getUserInfo(userDTO);
        if(one == null){
            one =new User();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);
        }else {
            throw new ServiceException(Constants.Code_600,"用户已存在");
        }
        return  one;
    }

    private  User getUserInfo(UserDTO userDTO){
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("fname",userDTO.getFname());
        queryWrapper.eq("remark",userDTO.getRemark());
        //其实queryWrapper和getOne的底层逻辑就是封装了这句sql：SELECT fid,fname,price,fcount,remark FROM t_fruit WHERE (fname = ? AND remark = ?)
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.Code_500,"系统错误");
        }
        return  one;
    }
}
