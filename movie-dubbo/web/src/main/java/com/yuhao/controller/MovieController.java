package com.yuhao.controller;

import com.yuhao.entity.Movie;
import com.yuhao.entity.ShowDay;
import com.yuhao.entity.ShowInfo;
import com.yuhao.entity.ShowTime;
import com.yuhao.service.MovieService;
import com.yuhao.util.AjaxResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("api")
public class MovieController {


    @Resource
    private MovieService movieService;

    //Spring封装了RedisTemplate对象来进行对Redis的各种操作，它支持所有的Redis原生的api。RedisTemplate位于spring-data-redis包下。
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 电影列表，按老师的意思，这里不能叫做方法了，应该叫做接口，一个和前端ajax对接的接口
     *
     * @return
     */

    @PostMapping("listMovies.do")
    @ResponseBody
    public AjaxResult listMovies() {

        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        //从redis中根据key"listMovies"查询值，电影信息。
        Object listMovies = stringObjectValueOperations.get("listMovies");
        //如果redis中有数据，直接返回前端，如果没有在55，57行重新查询数据库然后返回前端。
        if (listMovies != null) {
            return AjaxResult.success(listMovies);
        }

        List<Movie> movieList = movieService.listMovies();
        //往redis中set key value.   TimeUnit是设定key的存在时间，这里key存在24小时后就连带的value删除了，然后由上一行再从mysql查询。
        stringObjectValueOperations.set("listMovies", movieList, 24, TimeUnit.HOURS);

        return AjaxResult.success(movieList);

    }


    /**
     * 用id查找对应电影详情
     *
     * @param id
     * @return
     */
    @PostMapping("getMovieInfo.do")
    @ResponseBody
    public AjaxResult getMovieInfo(@RequestParam("id") Integer id) {
        return AjaxResult.success(movieService.selectByMoviePrimaryKey(id));
    }


    /**
     * 查询放映日期
     *
     * @param id 电影id
     * @return
     */
    @PostMapping("listShowDay.do")
    @ResponseBody
    public AjaxResult listShowDay(@RequestParam("id") Integer id) {
        return AjaxResult.success(movieService.listShowday(id));
    }

    /**
     * 查询放映时间段
     *
     * @param id 电影id
     * @return
     */
    @PostMapping("listShowTime.do")
    @ResponseBody
    public AjaxResult listShowTime(@RequestParam("id") Integer id) {
        List<List<ShowTime>> showTimeList = new ArrayList<>();   //ArrayList是基于数组实现的List(集合)类

        List<ShowDay> showDays = movieService.listShowday(id);   //通过电影Id在show_day表里查到集合 showDays

        for (ShowDay showDay : showDays) {
            List<ShowTime> showTimes = movieService.listShowTimeByDayId(showDay.getId());
            showTimeList.add(showTimes);
        }

        return AjaxResult.success(showTimeList);


    }


    /**
     * 查询选座页面信息
     *
     * @param
     * @return
     */
    @PostMapping("ShowInfo.do")
    @ResponseBody
    public AjaxResult ShowInfo(@RequestParam("showTimeId") Integer showTimeId) {

        ShowTime showTime = movieService.selectByShowTimePrimaryKey(showTimeId);
        ShowDay showDay = movieService.selectByShowDayPrimaryKey(showTime.getShowDayId());
        Movie movie = movieService.selectByMoviePrimaryKey(showDay.getMovieId());

        ShowInfo showInfo = new ShowInfo();
        showInfo.setMoviename(movie.getMoviename());
        showInfo.setPrice(movie.getPrice());
        showInfo.setDate(showDay.getShowDayCol());
        showInfo.setTime(showTime.getShowTimeCol());
        showInfo.setShowPlace(showTime.getShowPlace());
        showInfo.setLanguage(showTime.getLanguage());
        showInfo.setFilmlength(movie.getFilmlength());


        return AjaxResult.success(showInfo);
    }


}
