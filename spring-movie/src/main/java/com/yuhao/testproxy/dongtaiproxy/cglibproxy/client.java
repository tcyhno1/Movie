package com.yuhao.testproxy.dongtaiproxy.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;

public class client {
    public static void main(String[] args) {
        // 2. 然后在需要使用HelloConcrete的时候，通过CGLIB动态代理获取代理对象。
        Enhancer enhancer = new Enhancer();   //核心类
        enhancer.setSuperclass(HelloConcrete.class);   //确定父类
        enhancer.setCallback(new MyMethodInterceptor());    //设置回调函数

        HelloConcrete proxy = (HelloConcrete)enhancer.create();
        System.out.println(proxy.sayHello("I love you!"));
    }
}
