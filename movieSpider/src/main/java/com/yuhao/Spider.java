package com.yuhao;


import com.yuhao.service.BussService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 程序入口
 */
@Component
public class Spider {

    private static Logger log = LoggerFactory.getLogger(Spider.class);

    public static void main(String[] args) {
        log.info(">>>>>>>>>>spring上下文准备加载");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        log.info(">>>>>>>>>>spring上下文加载成功");


        BussService bussService = ctx.getBean("bussService", BussService.class);
        try {
            bussService.spiderMovie();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
