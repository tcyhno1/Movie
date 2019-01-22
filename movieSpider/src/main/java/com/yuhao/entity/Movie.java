package com.yuhao.entity;

public class Movie {
    private Integer id;

    private String moviename;

    private String director;

    private String writers;

    private String starring;

    private String type;

    private String producercountries;

    private String language;

    private String releasedate;

    private String filmlength;

    private String picpath;

    private String desc;

    private String price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename == null ? null : moviename.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers == null ? null : writers.trim();
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring == null ? null : starring.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getProducercountries() {
        return producercountries;
    }

    public void setProducercountries(String producercountries) {
        this.producercountries = producercountries == null ? null : producercountries.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate == null ? null : releasedate.trim();
    }

    public String getFilmlength() {
        return filmlength;
    }

    public void setFilmlength(String filmlength) {
        this.filmlength = filmlength == null ? null : filmlength.trim();
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath == null ? null : picpath.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", moviename='" + moviename + '\'' +
                ", director='" + director + '\'' +
                ", writers='" + writers + '\'' +
                ", starring='" + starring + '\'' +
                ", type='" + type + '\'' +
                ", producercountries='" + producercountries + '\'' +
                ", language='" + language + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", filmlength='" + filmlength + '\'' +
                ", picpath='" + picpath + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}