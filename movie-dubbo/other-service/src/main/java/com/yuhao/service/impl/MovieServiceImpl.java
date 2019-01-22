package com.yuhao.service.impl;


import com.yuhao.entity.Movie;
import com.yuhao.entity.ShowDay;
import com.yuhao.entity.ShowTime;
import com.yuhao.mapper.MovieMapper;
import com.yuhao.mapper.ShowDayMapper;
import com.yuhao.mapper.ShowTimeMapper;
import com.yuhao.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private ShowDayMapper showDayMapper;

    @Resource
    private ShowTimeMapper showTimeMapper;

    public List<Movie> listMovies() {

        List<Movie> movieList = movieMapper.listMovies();
        return movieList;

    }

    public Movie selectByMoviePrimaryKey(Integer id) {

        Movie movieList = movieMapper.selectByPrimaryKey(id);
        return movieList;

    }

    public ShowTime selectByShowTimePrimaryKey(Integer showDayId) {
        return showTimeMapper.selectByPrimaryKey(showDayId);
    }

    public ShowDay selectByShowDayPrimaryKey(Integer showDayId) {
        return showDayMapper.selectByPrimaryKey(showDayId);
    }

    public List<ShowDay> listShowday(Integer id) {

        List<ShowDay> movieList = showDayMapper.listShowday(id);
        return movieList;

    }

    public List<ShowTime> listShowTimeByDayId(Integer showDayId) {
        return showTimeMapper.listShowTimeByDayId(showDayId);
    }


}
