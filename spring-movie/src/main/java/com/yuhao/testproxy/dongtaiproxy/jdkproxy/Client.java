package com.yuhao.testproxy.dongtaiproxy.jdkproxy;


import com.yuhao.testproxy.staticproxy.RealSubject;
import com.yuhao.testproxy.staticproxy.Subject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {

        Subject delegate = new RealSubject();//被代理对象

        System.out.println(">>>>>>>>>>>>>不使用动态代理类");
        delegate.dealTask("bbbb");

        System.out.println(">>>>>>>>>>>>使用代理类执行");


        InvocationHandler handler = new SubjectInvocationHandler(delegate);//执行器

        //动态生成代理对象
        Subject proxy = (Subject) Proxy.newProxyInstance(
                delegate.getClass().getClassLoader(),
                delegate.getClass().getInterfaces(),
                handler);

        proxy.dealTask("DBQueryTask");


        System.out.println(">>>>>>>>>>>>代理另一个对象");

        Subject2 subject2 = new SubjectImpl2();//被代理的另一个对象

        InvocationHandler handler1 = new SubjectInvocationHandler(subject2);//执行器

        //动态生成代理对象
        Subject2 proxy1 = (Subject2) Proxy.newProxyInstance(
                subject2.getClass().getClassLoader(),
                subject2.getClass().getInterfaces(),
                handler1);

        proxy1.sayHello();


        System.out.println("代理对象名字："+proxy1.getClass().getName());



    }

}