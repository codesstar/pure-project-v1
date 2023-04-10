package com.example.demo.config;

import java.lang.annotation.*;

//自定义一个注解

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {

}
