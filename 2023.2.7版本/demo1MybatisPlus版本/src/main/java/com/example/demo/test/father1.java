package com.example.demo.test;

public interface father1 {
    public String nihao();

    default public String nibuhao(){
        return "methodFather";
    }
}
