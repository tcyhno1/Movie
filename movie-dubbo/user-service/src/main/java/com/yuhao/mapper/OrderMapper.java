package com.yuhao.mapper;

import com.yuhao.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    Order selectByPrimaryKey(Integer id);

    List<String> listSaledSeats(Integer showTimeId);

    int insertOrder(Order order);

    Order selectOrderForLock(@Param("orderNo") String orderNo);


    void updateOrderStatus(@Param("orderNo") String orderNo,
                           @Param("tradeNo") String tradeNo);

    List<Integer> listCancelOrders();

    Order selectByPrimaryKeyForLock(Integer id);

    void updateStatusById(Integer id);



    int countBySeat(@Param("showTimeId") Integer showTimeId,
                    @Param("seat") String seat);
}