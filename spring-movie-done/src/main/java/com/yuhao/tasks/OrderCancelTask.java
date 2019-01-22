package com.yuhao.tasks;


import com.yuhao.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动取消订单task
 */
@Component
public class OrderCancelTask {

    private static Logger log = LoggerFactory.getLogger(OrderCancelTask.class);

    @Resource
    private OrderService orderService;


    //注解设置定时时间
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void cancelOrder(){

        log.info(">>>>>>自动取消订单定时任务开始执行");
        orderService.cancelOrder();
        log.info(">>>>>>自动取消订单定时任务结束");
    }




}
