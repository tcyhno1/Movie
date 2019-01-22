package com.yuhao.mapper;

import com.yuhao.entity.Movie;

import java.util.List;

public interface MovieMapper {

    /**
     * 根据主键删除一条数据
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 全字段插入
     *
     * @param record
     * @return
     */
    int insert(Movie record);


    /**
     * 根据主键查询一条数据
     *
     * @param id
     * @return
     */
    Movie selectByPrimaryKey(Integer id);


    /**
     * 得到所有电影详情集合
     * @return
     */
    List<Movie> listMovies();


}