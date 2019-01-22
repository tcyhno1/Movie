package com.yuhao.mapper;

import com.yuhao.entity.ShowDay;

import java.util.List;

public interface ShowDayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShowDay record);



    ShowDay selectByPrimaryKey(Integer id);


    List<ShowDay> listShowday(Integer movieId);
}