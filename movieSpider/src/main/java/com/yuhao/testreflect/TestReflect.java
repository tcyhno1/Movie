package com.yuhao.testreflect;

import com.yuhao.entity.Movie;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestReflect {

    public static void main(String[] args) throws Exception {
        //反射重要资源就是  字节码
        //获得到字节码以后可以通过反射机制获取目标字节码属性、行为
        //还可以执行目标方法

        //获取字节码的三种方式
        //第一种
        Class clazz1 = Movie.class;
        //第二种
        Class clazz2 = Class.forName("com.yuhao.entity.Movie");
        //第三种
        Movie movie = new Movie();
        Class clazz3 = movie.getClass();

        System.out.println("通过字节码获取名称"+clazz1.getName());  //连带路径的全称
        System.out.println("通过字节码获取名称"+clazz1.getSimpleName()); //类名

        /**
         * getFields()只能获取public的字段，包括父类的。
         * 而getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
         */
//
        //获得属性
        Field[] fields = clazz1.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(String.format("%s %s %s", Modifier.toString(f.getModifiers()),f.getType().getSimpleName(),f.getName()));//属性修饰符，属性类型，属性名
            System.out.println(String.format("%s %s %s",f.getModifiers(),f.getType(),f.getName()));//属性修饰符，属性类型，属性名
        }

        //获得方法
        Method[] methods = clazz1.getDeclaredMethods();
        for (Method m : methods){
            System.out.println(m.getName()); //获取方法的方法名
            System.out.println(Modifier.toString(m.getModifiers()));  //获取方法的修饰符
            System.out.println(m.getParameterTypes()); //返回参数类型[数组]
            Class[] types = m.getParameterTypes();//获取传入参数类型，返回一个数组，需要遍历才能拿到，如下
            for (Class t : types){
                System.out.println(t.getName());
            }
        }

        System.out.println("_________________________________________");

        //用反射的方法给一个实例对象set属性值。
        // 第一种：先通过字节码Class拿到方法setMoviename，然后用.invoke(实例对象，传值)为其属性赋值
        Method setMoviename = clazz1.getDeclaredMethod("setMoviename", String.class);// String.class :在JAVA中，一般获得某个参数方法都先要将其转换成字符串的格式读取出来.
        Object obj = clazz1.newInstance();
        setMoviename.invoke(obj,"我不是药神");
        Movie movie1 = (Movie)obj;
        System.out.println(movie1.getMoviename());

        //第二种：通过字节码Class拿到属性moviename，然后用属性的set方法为其赋值。
        //注意，如果属性是private的，需要.setAccessible(true)，然后再进行赋值。
        Field moviename = clazz1.getDeclaredField("moviename"); //拿到属性字段
        moviename.setAccessible(true);//可以设置私有变量
        moviename.set(obj,"一出好戏");//属性赋值
        movie1 = (Movie)obj;
        System.out.println(movie1.getMoviename());
    }
}
