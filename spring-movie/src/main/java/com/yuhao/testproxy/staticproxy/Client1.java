package com.yuhao.testproxy.staticproxy;

public class  Client1 {

    public static void main(String[] args) {
        //使用工厂类生成一个代理类
//        Subject proxy = SubjectStaticFactory.getInstance();
//        proxy.dealTask("DBQueryTask");


        Subject realSubject = new RealSubject();
        Subject proxySubject = new ProxySubject(realSubject);
        proxySubject.dealTask("会议内容");
    }

}