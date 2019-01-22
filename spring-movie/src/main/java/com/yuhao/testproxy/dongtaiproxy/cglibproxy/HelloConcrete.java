package com.yuhao.testproxy.dongtaiproxy.cglibproxy;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HelloConcrete {

    @Transactional(value = "123123",timeout=2)
    @MyAnn()
    public String sayHello(String str) {
        System.out.println("sayhello "+str);
        return "目标方法返回内容" ;
    }
}