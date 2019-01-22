package com.yuhao.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class DbUtil {
    static SqlSessionFactory sqlSessionFactory = null;

    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("加载mybatis失败");
        }
    }


    public static SqlSession getSqlSession() {
        return getSqlSession(true);
    }

    public static SqlSession getSqlSession(boolean isAutoCommit) {
        return sqlSessionFactory.openSession(isAutoCommit);
    }


    public static void closeAll(AutoCloseable... objs) {
        for (AutoCloseable obj : objs) {
            try {
                if (obj != null) {
                    obj.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
