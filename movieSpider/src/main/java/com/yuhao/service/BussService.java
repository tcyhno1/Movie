package com.yuhao.service;


import com.yuhao.entity.Movie;
import com.yuhao.mapper.MovieMapper;
import com.yuhao.util.HttpRequestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BussService {

    private static Logger log = LoggerFactory.getLogger(BussService.class);

    @Autowired
    private MovieMapper movieMapper;


    @Autowired
    private HttpClientService httpClientService;

    public void spiderMovie() throws IOException {

        log.info("正在分析。。。");

        //获取南京所有正在热映的电影html文档
        String html = httpClientService.doGet("https://movie.douban.com/cinema/nowplaying/nanjing/");
        log.info("南京正在热映电影：{}", html);
        //jsoup解析
        Document doc = Jsoup.parse(html);

        Elements moviesEles = doc.select("#nowplaying > div.mod-bd > ul > li");
        log.info("查询到电影总数：" + moviesEles.size());

        for (Element movieEle : moviesEles) {
            Movie movie = new Movie();

            log.info(">>>>>" + movieEle.html());

            //电影名称 ,这个爬到的名称有...，因为是在首页上，名字打不全
//            String movieName = movieEle.select("li.stitle > a").text();
//            movie.setMoviename(movieName);

            //这个通过一个title属性拿到全名
//            String movieName1 = movieEle.select("li.stitle > a").attr("title");
////            System.out.println("__________>>>>>>>>____________"+movieName1);
//            movie.setMoviename(movieName1);

            //图片
            String img = movieEle.select("li.poster > a > img").attr("src");
            String imgName = img.substring(img.lastIndexOf("/"));//    </p2529570494.jpg>
            String dirPath = "E:\\movies-pic\\";   //   <E:\movies-pic\>
            HttpRequestUtils.downloadPicture(img, dirPath, imgName, 2000);
            String s = dirPath+imgName;       //    <E:\movies-pic\/p2529570494.jpg>  select substring_index(picPath,'/',-1) from movie;    UPDATE movie set picPath=substring_index(picPath,'/',-1) ;
            movie.setPicpath(dirPath + imgName);


            //再请求详情，这里是通过href属性拿到了每个电影的链接，存到movieInfoUrl
            String movieInfoUrl = movieEle.select("li.poster > a").attr("href");

            //获得指定电影的详情页面，从这里开始已经不是刚才的页面了
            String movieInfoHtml = httpClientService.doGet(movieInfoUrl);
            //jsoup解析
            Document movidInfoDoc = Jsoup.parse(movieInfoHtml);

            //爬简介
            String desc = movidInfoDoc.select("#link-report > span").text();
            movie.setDesc(desc);

            String movieName2 = movidInfoDoc.select("#content > h1 > span:nth-child(1)").text();
            movie.setMoviename(movieName2);


            Element info = movidInfoDoc.select("#info").first();
            Elements spans = info.select("> span");
            for (Element span : spans) {
                if (span.childNodeSize() > 0) {
                    String key = span.getElementsByAttributeValue("class", "pl").text();
                    if ("导演".equals(key)) {
                        movie.setDirector(span.getElementsByAttributeValue("class", "attrs").text());
                    } else if ("编剧".equals(key)) {
                        movie.setWriters(span.getElementsByAttributeValue("class", "attrs").text());

                    } else if ("主演".equals(key)) {
                        movie.setStarring(span.getElementsByAttributeValue("class", "attrs").text());

                    } else if ("类型:".equals(key)) {
                        movie.setType(movidInfoDoc.getElementsByAttributeValue("property", "v:genre").text());

                    } else if ("制片国家/地区:".equals(key)) {
                        Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>.+\\s+<br>");
//                        Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>(.+\\s+)<br>");
                        Matcher matcherCountry = patternCountry.matcher(movidInfoDoc.html());
                        if (matcherCountry.find()) {
                            movie.setProducercountries(matcherCountry.group().split("</span>")[1].split("<br>")[0].trim());// for example: >制片国家/地区:</span> 中国大陆 / 香港     <br>
//                            movie.setProducercountries(matcherCountry.group(1));
                        }
                    } else if ("语言:".equals(key)) {
                        Pattern patternLanguage = Pattern.compile(".语言:</span>.+\\s+<br>");
//                        Pattern patternLanguage = Pattern.compile(".语言:</span>(.+\\s+)<br>");
                        Matcher matcherLanguage = patternLanguage.matcher(movidInfoDoc.html());
                        if (matcherLanguage.find()) {
                            movie.setLanguage(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
//                            movie.setLanguage(matcherLanguage.group(1));
                        }
                    } else if ("上映日期:".equals(key)) {
                        movie.setReleasedate(movidInfoDoc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());

                    } else if ("片长:".equals(key)) {
                        movie.setFilmlength(movidInfoDoc.getElementsByAttributeValue("property", "v:runtime").text());

                    }
                }
            }

            log.info(">>>>>>>>{}", movie);
            movieMapper.insert(movie);

        }


    }

    public void FanxingFanshe(){
        System.out.println("通过泛型和反射得到实例对象");
    }

}
