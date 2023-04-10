package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username= userDTO.getFname();
        String remark=userDTO.getRemark();

        if(StrUtil.isBlank(username)||StrUtil.isBlank(remark)){
            return Result.error(Constants.Code_400,"参数错误");
        }
        UserDTO dto=userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username= userDTO.getFname();
        String remark=userDTO.getRemark();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(remark)){
            return Result.error(Constants.Code_400,"参数错误");
        }
        return Result.success(userService.register(userDTO));
    }
    @GetMapping("/username/{username}")
    public Result findone(@PathVariable String username) {
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("fname",username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @PostMapping
    public Result save(@RequestBody User user){
        return Result.success(userService.saveUser(user));
    }

    @GetMapping
    public List<User> findAll() {

//        return userMapper.findAll();
        return  userService.list();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        System.out.println("nihao");
        return userService.removeByIds(ids);
    }

    //这个是直接在斜杆后面的形式
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
//        return userMapper.deleteById(id);
        return userService.removeById(id);
    }

//    @GetMapping("/page")
//    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String fname) {
//       pageNum=(pageNum - 1) * pageSize;
//       fname="%"+fname+"%";
//       Integer total=userMapper.selectTotal(fname);
//       List<User> data=userMapper.selectPage(pageNum,pageSize,fname);
//       Map<String,Object> res =new HashMap<>();
//       res.put("data",data);
//       res.put("total",total);
//       return  res;
//    }


    //mybatisplus分页查询
    //requestBody是传入一个对象，param是传入参数，？a=1&b=2那种形式
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String fname,
                                        @RequestParam(defaultValue = "") String remark
    ) {
     IPage<User>page =new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        if(!"".equals(fname)){
            queryWrapper.like("fname",fname);
        }
        if(!"".equals(remark)){
            queryWrapper.like("remark",remark);
        }
        User currentUser= TokenUtils.getCurrentUser();


        queryWrapper.orderByDesc("fid");
        return  userService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        //把excel中的内容转化为实体类User的一个list，不过要求excel中表明和user类表名要对应!!!，如果不对应，就用下面的方式
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);

//        // 方式2：忽略表头的中文，直接读取表的内容
//        从第一行开始读
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }
//        userService.saveBatch(users);

        return true;
    }
}
