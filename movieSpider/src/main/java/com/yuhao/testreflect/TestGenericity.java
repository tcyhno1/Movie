package com.yuhao.testreflect;

import com.yuhao.service.BussService;
import com.yuhao.service.HttpClientService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGenericity {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        //手写ArrayList测试，不过没有实现大部分功能，尤其是toArray
        MyList<Integer> myList = new MyList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(4);
            add(5);
            add(6);
            add(7);
        }};
        myList.add(8);
        System.out.println(myList.size());

        System.out.println("----------------");

        //手写getBean
        MyBeanFactory myBeanFactory = new MyBeanFactory();
        BussService bussService = myBeanFactory.getBean("bussService", BussService.class);
        bussService.FanxingFanshe();


    }


}

/**
 * 尝试泛型类
 * 手写ArrayList
 *
 * @param <T>
 */
class MyList<T> { //定义泛型类

    private int index = 0;
    private Object[] objects = null;

    public MyList(){
        objects = new Object[7];
    }

    public MyList(int size){
        objects = new Object[size];
    }

    public void add(T value) {
        //如果List被占用超过70%就扩展一倍；也就是新建一个大一倍的新List,把旧的复制过去，旧的没有引用会被垃圾回收掉。
        double g = (index +1)/Double.valueOf(objects.length);
        if (g >=0.7) {
            objects= Arrays.copyOf(objects,objects.length*2);
        }
        objects[index++] = value;

    }

    public T get(int index) {
        if (index>objects.length){
            return null;
        }

        return (T)objects[index];
    }

    public int size(){
        return index;
    }

}

/**
 * 尝试泛型方法
 * 手写spring getBean
 */
class MyBeanFactory {

    Map<String, Object> beans =new HashMap<String,Object>(){{
        put("bussService", new BussService());
        put("httpClientService", new HttpClientService());
    }};

    public <K> K getBean(String beanName,Class<K> clazz) throws IllegalAccessException, InstantiationException {
//        return (K)beans.get(beanName);
        return (K) clazz.newInstance();
    }




}