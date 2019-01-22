package com.yuhao.service;

import com.alipay.api.AlipayApiException;
import com.yuhao.entity.Order;
import com.yuhao.exception.BizException;

import java.util.List;

public interface OrderService {

    List<String> listSaledSeats(Integer showTimeId);

    Order selectOrderForLock(String orderNo);

    Order addOrder(Integer showTimeId, Integer movieId, List<String> selectedSeats, Integer userId) throws BizException;

    String toPay(String orderNo, String money) throws AlipayApiException;

    void updateOrderStatus(String out_trade_no, String trade_no) throws Exception;

    void cancelOrder();


}
