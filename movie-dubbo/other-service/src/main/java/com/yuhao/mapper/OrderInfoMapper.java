package com.yuhao.mapper;

import com.yuhao.entity.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);

    void insertOderInfo(OrderInfo orderInfo);


}