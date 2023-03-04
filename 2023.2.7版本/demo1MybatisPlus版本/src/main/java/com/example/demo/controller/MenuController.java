package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;



    @PostMapping
    public Result save(@RequestBody Role role){
        return Result.success(roleService.saveOrUpdate(role));
    }

    @GetMapping
    public Result findAll() {


        return  Result.success(roleService.list());
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        System.out.println("nihao");
        return Result.success(roleService.removeByIds(ids));
    }

    //这个是直接在斜杆后面的形式
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(roleService.removeById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String description
    ) {
        IPage<Role>page =new Page<Role>(pageNum,pageSize);
        QueryWrapper<Role> queryWrapper =new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(description)){
            queryWrapper.like("description",description);
        }
        User currentUser= TokenUtils.getCurrentUser();

        queryWrapper.orderByDesc("id");
        return  Result.success(   roleService.page(page,queryWrapper));
    }


}
