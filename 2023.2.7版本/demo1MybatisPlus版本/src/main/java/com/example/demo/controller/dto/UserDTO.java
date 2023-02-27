package com.example.demo.controller.dto;

import lombok.Data;


@Data
public class UserDTO {
    private String fname;
    private String remark;
    private Integer price;
    private Integer fcount;
    private String  token;
    private String  avatarUrl;
}
