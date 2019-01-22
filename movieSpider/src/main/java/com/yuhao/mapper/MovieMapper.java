package com.yuhao.mapper;

import com.yuhao.entity.Movie;

import java.util.List;

public interface MovieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Movie record);

    int insertSelective(Movie record);

    Movie selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Movie record);

    int updateByPrimaryKey(Movie record);

    /**
     * 查询所有电影
     * @return
     */
    List<Movie> listMovie();
}