package com.yuhao.testproxy.dongtaiproxy.cglibproxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

// CGLIB动态代理
// 1. 首先实现一个MethodInterceptor，方法调用会被转发到该类的intercept()方法。
class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        System.out.println("代理执行的方法"+method.getName());

        //方法头上是否有Transactional注解声明
        boolean isHasTran = method.isAnnotationPresent(MyAnn.class);
        if(isHasTran){//需要事务
            MyAnn annotation = method.getAnnotation(MyAnn.class);
            System.out.println(annotation.value());
//            System.out.println(annotation.timeout());

            System.out.println("开启事务");

//            Object object =method.invoke(new HelloConcrete(),args);
            Object object = proxy.invokeSuper(obj, args);//目标代码
            return object;
        }else{
            Object object = proxy.invokeSuper(obj, args);//目标代码
            return object;
        }


    }
}