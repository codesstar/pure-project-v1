package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Dict;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;

import com.example.demo.mapper.DictMapper;
import com.example.demo.service.MenuService;

import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private MenuService menuService;

    @Resource
    private DictMapper dictMapper;



    @PostMapping
    public Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveOrUpdate( menu));
    }

    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "")String name) {


        return  Result.success( menuService.findMenus(name));
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        System.out.println("nihao");
        return Result.success(menuService.removeByIds(ids));
    }

    //这个是直接在斜杆后面的形式
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(menuService.removeById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String path,
                                @RequestParam(defaultValue = "") String description
    ) {
        IPage<Menu>page =new Page<Menu>(pageNum,pageSize);
        QueryWrapper<Menu> queryWrapper =new QueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(path)){
            queryWrapper.like("path",path);
        }
        if(!"".equals(description)){
            queryWrapper.like("description",description);
        }
//        User currentUser= TokenUtils.getCurrentUser();

//        queryWrapper.orderByDesc("id");
        return  Result.success(  menuService.page(page,queryWrapper));
    }

    @GetMapping("/icons")
    public  Result getIcons(){
        QueryWrapper<Dict> queryWrapper =new QueryWrapper<Dict>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }

    @GetMapping("/demo")
    public  Result demo(){

      return Result.success(menuService.demo());
    }


}
