package com.yuhao.mapper;

import com.yuhao.entity.ShowTime;

import java.util.List;

public interface ShowTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShowTime record);



    ShowTime selectByPrimaryKey(Integer id);


    List<ShowTime> listShowTimeByDayId(Integer showDayId);

}