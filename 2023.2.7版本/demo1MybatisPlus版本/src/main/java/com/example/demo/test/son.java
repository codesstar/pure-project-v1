package com.example.demo.test;

public class son implements  father1{

    @Override
    public String nihao() {
        String abc=nibuhao();
        String kd="nihao"+abc;
        return kd;
    }
//    public String nibuhao(){
//        return "wotamain";
//    }

    public String methodSon(){
        return "methodson+";
    }


    public static void main(String[] args) {
        son son1 =new son();
        System.out.println(son1.nihao());

    }

}
