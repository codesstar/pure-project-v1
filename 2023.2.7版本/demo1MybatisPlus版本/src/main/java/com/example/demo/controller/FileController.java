package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import java.util.List;



    /**
     * 文件上传相关接口
     */
    @RestController
    @RequestMapping("/file")
    public class FileController {

        @Value("${files.upload.path}")
        private String fileUploadPath;

        @Resource
        private FileMapper fileMapper;

        /**
         * 文件上传接口
         * @param file 前端传递过来的文件
         * @return
         * @throws IOException
         */
        @PostMapping("/upload")
        public String upload(@RequestParam MultipartFile file) throws IOException {
            //上传一个文件，得到文件的原始名字
            String originalFilename = file.getOriginalFilename();
            //得到文件的扩展名，也就就是文件类型
            String type = FileUtil.extName(originalFilename);
            //得到文件的大小
            long size = file.getSize();

            // 定义一个文件唯一的标识码,也是作为文件存储的名称
            String uuid = IdUtil.fastSimpleUUID();
            String fileUUID = uuid + StrUtil.DOT + type;


            //新建立一个文件，或者说新建立一个目录，这个目录就是我们的定义好的fileUploadPath路径（写在application配置文件中）
            //加上我们文件的uuid，其实就是在相应路径创建一个文件，只需要传入一个路劲就好了，文件可以先没内容，或者说这一步只是创建了相应路径中的一个空文件罢了
            //uploadFile就是这个空文件
            File uploadFile = new File(fileUploadPath + fileUUID);
            // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
            File parentFile = uploadFile.getParentFile();
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //原本直接把上传的文件传到我们写好的路径中的空文件就好了，但是出现一个问题，就是两个相同的文件可能它的名称不同
            //这时候它们的uuid不同，在磁盘中会创建两个不同的文件，占据了不必要的空间。如果文件是相同的而名称不同，我们应该将其只存储一份。
            //但是有什么可以判断文件内容是否相同呢？有的，就是文件的md5，两个相同文件的md5就是相同的。
            String url;
            // 获取文件的md5
            String md5 = SecureUtil.md5(file.getInputStream());
            // 从数据库查询是否存在相同的记录，getFileByMd5方法写在下面
            Files dbFiles = getFileByMd5(md5);
            if (dbFiles != null) { // 文件已存在
                url = dbFiles.getUrl();//文件存在的时候，直接从数据库中查出它的路径就好了
            } else {
                // 上传文件到磁盘，将文件转移到刚创建的空文件那里
                file.transferTo(uploadFile);
                // 数据库若不存在重复文件，则不删除刚才上传的文件
                url = "http://localhost:9090/file/" + fileUUID;
            }

            // 存储数据库   这里的逻辑是相同文件不同名字也存储到数据库，其实没必要
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size/1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);

            return url;
        }

        /**
         * 文件下载接口   http://localhost:9090/file/{fileUUID}
         * @param fileUUID
         * @param response
         * @throws IOException
         */
        @GetMapping("/{fileUUID}")
        public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
            // 根据文件的唯一标识码获取文件
            File uploadFile = new File(fileUploadPath + fileUUID);
            // 设置输出流的格式
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
            response.setContentType("application/octet-stream");

            // 读取文件的字节流
            os.write(FileUtil.readBytes(uploadFile));
            os.flush();
            os.close();
        }


        /**
         * 通过文件的md5查询文件
         * @param md5
         * @return
         */
        private com.example.demo.entity.Files getFileByMd5(String md5) {
            // 查询文件的md5是否存在
            QueryWrapper<com.example.demo.entity.Files> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("md5", md5);
            List<com.example.demo.entity.Files> filesList = fileMapper.selectList(queryWrapper);
            return filesList.size() == 0 ? null : filesList.get(0);
        }

        @PostMapping("/update")
        public Result update(@RequestBody Files files) {
            return Result.success(fileMapper.updateById(files));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            Files files = fileMapper.selectById(id);
            files.setIsDelete(true);
            fileMapper.updateById(files);
            return Result.success();
        }

        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids) {
            // select * from sys_file where id in (id,id,id...)
            QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", ids);
            List<Files> files = fileMapper.selectList(queryWrapper);
            for (Files file : files) {
                file.setIsDelete(true);
                fileMapper.updateById(file);
            }
            return Result.success();
        }

        /**
         * 分页查询接口
         * @param pageNum
         * @param pageSize
         * @param name
         * @return
         */
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String name) {

            QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
            // 查询未删除的记录
            queryWrapper.eq("is_delete", false);
            queryWrapper.orderByDesc("id");
            if (!"".equals(name)) {
                queryWrapper.like("name", name);
            }
            return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
        }
    }

