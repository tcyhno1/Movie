package com.yuhao.spider;

import com.yuhao.util.HttpRequestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderDouban {

    public static void main(String[] args) {
        String url = "https://movie.douban.com/cinema/nowplaying/nanjing/";

        try {
            String html = HttpRequestUtils.httpGet(url, new HashMap<>(), 2000);


            //jsoup解析
            Document doc = Jsoup.parse(html);

            Elements moviesEles = doc.select("#nowplaying > div.mod-bd > ul > li");
            System.out.println(moviesEles.size());
            for (Element movieEle : moviesEles) {
                System.out.println(">>>>>" + movieEle.html());

                //电影名称
                String movieName = movieEle.select("li.stitle > a").text();
                System.out.println(movieName);

                //图片
                String img = movieEle.select("li.poster > a > img").attr("src");
                String imgName = img.substring(img.lastIndexOf("/"));
                HttpRequestUtils.downloadPicture(img, "E:\\movies-pic\\", imgName, 2000);


                //再请求详情
                //再请求详情
                String movieInfoUrl = movieEle.select("li.poster > a").attr("href");
//                System.out.println("movieInfoUrl>>>>" + movieInfoUrl);
                String movieInfoHtml = HttpRequestUtils.httpGet(movieInfoUrl, new HashMap<>(), 2000);
                //jsoup解析
                Document movidInfoDoc = Jsoup.parse(movieInfoHtml);

                Element info = movidInfoDoc.select("#info").first();
                Elements spans = info.select("> span");
                for (Element span : spans) {
                    if (span.childNodeSize() > 0) {
                        String key = span.getElementsByAttributeValue("class", "pl").text();
                        if ("导演".equals(key)) {
//                            movie.setDirector(span.getElementsByAttributeValue("class", "attrs").text());
                            System.out.println("导演：" + span.getElementsByAttributeValue("class", "attrs").text());
                        } else if ("编剧".equals(key)) {
//                            movie.setWriters(span.getElementsByAttributeValue("class", "attrs").text());
                            System.out.println("编剧：" + span.getElementsByAttributeValue("class", "attrs").text());

                        } else if ("主演".equals(key)) {
//                            movie.setStarring(span.getElementsByAttributeValue("class", "attrs").text());
                            System.out.println("主演：" + span.getElementsByAttributeValue("class", "attrs").text());

                        } else if ("类型:".equals(key)) {
//                            movie.setType(doc.getElementsByAttributeValue("property", "v:genre").text());
                            System.out.println("类型：" + movidInfoDoc.getElementsByAttributeValue("property", "v:genre").text());

                        } else if ("制片国家/地区:".equals(key)) {
                            Pattern patternCountry = Pattern.compile(".制片国家/地区:</span>.+\\s+<br>");
                            Matcher matcherCountry = patternCountry.matcher(movidInfoDoc.html());
//                            System.out.println("=========="+movidInfoDoc.html());
                            if (matcherCountry.find()) {
//                                movie.setProducerCountries(matcherCountry.group().split("</span>")[1].split("<br>")[0].trim());// for example: >制片国家/地区:</span> 中国大陆 / 香港     <br>
                                String group = matcherCountry.group();
                                System.out.println("matcherCountry.group():" + group);
                                System.out.println("制片国家/地区:" + group.split("</span>")[1].split("<br>")[0].trim());

                            }
                        } else if ("语言:".equals(key)) {
                            Pattern patternLanguage = Pattern.compile(".语言:</span>.+\\s+<br>");
                            Matcher matcherLanguage = patternLanguage.matcher(movidInfoDoc.html());
                            if (matcherLanguage.find()) {
//                                movie.setLanguage(matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());
                                System.out.println("语言：" + matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim());

                            }
                        } else if ("上映日期:".equals(key)) {
//                            movie.setReleaseDate(doc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());
                            System.out.println("上映日期：" + movidInfoDoc.getElementsByAttributeValue("property", "v:initialReleaseDate").text());

                        } else if ("片长:".equals(key)) {
//                            movie.setFilmLength(doc.getElementsByAttributeValue("property", "v:runtime").text());
                            System.out.println("片长：" + movidInfoDoc.getElementsByAttributeValue("property", "v:runtime").text());

                        }
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
