package com.yuhao.service;

import com.yuhao.entity.Movie;
import com.yuhao.entity.ShowDay;
import com.yuhao.entity.ShowTime;

import java.util.List;

public interface MovieService {


    List<Movie> listMovies();

    Movie selectByMoviePrimaryKey(Integer id);

    ShowTime selectByShowTimePrimaryKey(Integer showDayId);

    ShowDay selectByShowDayPrimaryKey(Integer showDayId);

    List<ShowDay> listShowday(Integer id);

    public List<ShowTime> listShowTimeByDayId(Integer showDayId);
}
