package com.example.demo.test;

public class son2 extends son{

    public String son2Method(){
        String k=methodSon();
        String g=nihao();
        String s=nibuhao();
        System.out.println(k+g+s);
        return k+g+s;
    }

    public static void main(String[] args) {
        son2 son222=new son2();
        son222.son2Method();
    }

}
