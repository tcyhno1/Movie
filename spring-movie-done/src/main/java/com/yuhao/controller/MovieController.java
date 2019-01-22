package com.yuhao.controller;

import com.yuhao.entity.Movie;
import com.yuhao.entity.ShowDay;
import com.yuhao.entity.ShowInfo;
import com.yuhao.entity.ShowTime;
import com.yuhao.mapper.MovieMapper;
import com.yuhao.mapper.ShowDayMapper;
import com.yuhao.mapper.ShowTimeMapper;
import com.yuhao.util.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api")
public class MovieController {

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private ShowDayMapper showDayMapper;

    @Resource
    private ShowTimeMapper showTimeMapper;

    /**
     * 电影列表，这里不能叫做方法了，应该叫做接口，一个和前端ajax对接的接口
     *
     * @return
     */

    @PostMapping("listMovies.do")
    @ResponseBody
    public AjaxResult listMovies() {
        return AjaxResult.success(movieMapper.listMovies());
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
        return AjaxResult.success(movieMapper.selectByPrimaryKey(id));
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
        return AjaxResult.success(showDayMapper.listShowday(id));
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

        List<ShowDay> showDays = showDayMapper.listShowday(id);   //通过电影Id在show_day表里查到集合 showDays

        for (ShowDay showDay : showDays) {
            List<ShowTime> showTimes = showTimeMapper.listShowTimeByDayId(showDay.getId());
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

        ShowTime showTime = showTimeMapper.selectByPrimaryKey(showTimeId);
        ShowDay showDay = showDayMapper.selectByPrimaryKey(showTime.getShowDayId());
        Movie movie = movieMapper.selectByPrimaryKey(showDay.getMovieId());

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
