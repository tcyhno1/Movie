package com.yuhao.spider;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.List;

public class TestHttpClient {

    public static void main1(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();  //第一步：创建一个HttpClient对象；使用CloseableHttpClient的工厂类HttpClients的方法来创建实例;最简单的实例化方式是调用HttpClients.createDefault()。；HttpClients.createDefault()实际上调用的也就是HttpClientBuilder.create().build()。
        try {
            HttpGet httpget = new HttpGet("http://localhost:8080/login.jsp");  //第二步：创建一个Request对象；Request的对象建立很简单，一般用目标url来构造就好了

            System.out.println("Executing request " + httpget.getURI());   //Executing request http://localhost:8080/login.jsp
            CloseableHttpResponse response = httpclient.execute(httpget);  //第三步：执行Request请求；执行Request请求就是调用HttpClient的execute方法。
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());               //HTTP/1.1 200  //第四步：处理response；HttpReaponse是将服务端发回的Http响应解析后的对象
                System.out.println(EntityUtils.toString(response.getEntity()));  // <html>...</html> //EntityUtils中有个toString方法也很方便的（调用这个方法最后也会自动把inputStream close掉的），不过只有在可以确定收到的entity不是特别大的情况下才能使用。
                // Do not feel like reading the response body
                // Call abort on the request object
                httpget.abort();         //释放连接资源，很重要，不能缺少
            } finally {
                response.close();   //Close断开客户端的连接。执行response.close()虽然会正确释放掉该connection占用的所有资源，但是这是一种比较暴力的方式，采用这种方式之后，这个connection就不能被重复使用了。
            }

        } finally {

            httpclient.close();  //第五步：关闭HttpClient
        }
    }

    public static void main(String[] args) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom()  //第一步：创建一个HttpClient对象;使用CloseableHttpClient的builder类HttpClientBuilder，先对一些属性进行配置（采用装饰者模式，不断的.setxxxxx().setxxxxxxxx()就行了），再调用build方法来创建实例。
                .setDefaultCookieStore(cookieStore)             //HttpClients.custom() 的底层就是返回一个HttpClientBuilder.create();
                .build();
        try {
            //访问login.jsp
            HttpGet httpget = new HttpGet("http://localhost:8080/login.jsp"); //第二步：创建一个Request对象
            CloseableHttpResponse response1 = httpclient.execute(httpget); //第三步：执行Request请求
            try {
                HttpEntity entity = response1.getEntity();   // <html>...</html>

                System.out.println("Login form get1: " + response1.getStatusLine()); //Login form get: HTTP/1.1 200
                EntityUtils.consume(entity);    //释放资源   最主要的就是consume()这个方法，其功能就是关闭HttpEntity的流

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("-1 " + cookies.get(i).toString());  //-1 [version: 0][name: JSESSIONID][value: 7DBFB3D049EB3EA881DB688D5EEFE5C7][domain: localhost][path: /][expiry: null]
                    }
                }
            } finally {
                response1.close();
            }


            //通过抓包，找到登录接口和form表单参数，并发送登录请求
            //相当于用户在浏览器点击登录按钮
            //此步骤执行成功后，用户登录成功，会话会保持
            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("http://localhost:8080/user/login.do"))
                    .addParameter("loginName", "108")
                    .addParameter("password", "1234")
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get2: " + response2.getStatusLine()); //Login form get2: HTTP/1.1 302
                EntityUtils.consume(entity);

                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("-2 " + cookies.get(i).toString());//-2 [version: 0][name: JSESSIONID][value: 7DBFB3D049EB3EA881DB688D5EEFE5C7][domain: localhost][path: /][expiry: null]
                    }
                }
            } finally {
                response2.close();
            }




            String  html = null;
            //请求home.do
            httpget = new HttpGet("http://localhost:8080/user/home.do");
            response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get3: " + response1.getStatusLine());  //Login form get3: HTTP/1.1 200
//                System.out.println(EntityUtils.toString(response1.getEntity()));  //toString 可以关闭stream
                html = EntityUtils.toString(response1.getEntity());//html文本
                EntityUtils.consume(entity);//消费

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("-3 " + cookies.get(i).toString()); //-3 [version: 0][name: JSESSIONID][value: 7DBFB3D049EB3EA881DB688D5EEFE5C7][domain: localhost][path: /][expiry: null]
                    }
                }
            } finally {
                response1.close();
            }


            String  html1 = null;
            //请求home.do
            httpget = new HttpGet("http://localhost:8080/balance/streams.do");
            response1 = httpclient.execute(httpget);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get3: " + response1.getStatusLine());  //Login form get3: HTTP/1.1 200
//                System.out.println(EntityUtils.toString(response1.getEntity()));  //toString 可以关闭stream
                html1 = EntityUtils.toString(response1.getEntity());//html文本
                EntityUtils.consume(entity);//消费

                System.out.println("Initial set of cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                    System.out.println();
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("-3 " + cookies.get(i).toString()); //-3 [version: 0][name: JSESSIONID][value: 7DBFB3D049EB3EA881DB688D5EEFE5C7][domain: localhost][path: /][expiry: null]
                    }
                }
            } finally {
                response1.close();
            }





            //jsoup解析
            Document doc = Jsoup.parse(html);
            Element userName = doc.getElementById("wrapper");
            System.out.println(userName.text());

            Elements p = doc.select("body > div > div:nth-child(3) > div:nth-child(3) > div > div.details > div.desc");
            System.out.println(p.text());



            Document doc1 = Jsoup.parse(html1);
            Element content = doc1.getElementById("streams_table");
            Elements links = content.getElementsByTag("a");
            for (Element link : links) {
//                String linkHref = link.attr("href");
//                System.out.println(linkHref.length());
//                String linkText = link.text();
//                System.out.println(linkText.length());
                System.out.println(link);
            }


        } finally {
            httpclient.close();
        }
    }


}
