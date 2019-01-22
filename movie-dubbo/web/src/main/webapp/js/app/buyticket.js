var app = new Vue({
    el: '#app',
    data: {
        movie: {},
        showDays:[],
        showTimeList:[]
    },
    methods:{
        toSeat:function (showTimeId,movieId) {
            window.location.href='/seat.html?showTimeId='+showTimeId+"&"+"movieId="+movieId;
        }
    },

    created: function () {
        //获取页面传值
        var id = UrlParam.param("id");

        //获取电影信息
        $.ajax({
            type: 'post', //默认get
            url: "/api/getMovieInfo.do",   //请求的目标，从该处请求出所需要的json数据
            data: {id: id},    //向后端发请求时，携带的数据
            async: true,   //是否异步（默认true：异步）
            dataType: "json",//定义服务器返回的数据类型
            success: function (data) {//data服务器返回的json字符串
                if (data.success) {
                    app.movie = data.data;
                } else {
                    alert(data.errorMsg)
                }

            }
        });


        //请求放映日期时间
        $.ajax({
            type: 'post', //默认get
            url: "/api/listShowDay.do",
            data: {id: id},
            async: true,   //是否异步（默认true：异步）
            dataType: "json",//定义服务器返回的数据类型
            success: function (data) {//data服务器返回的json字符串
                if (data.success) {

                    app.showDays=data.data;

                } else {
                    alert(data.errorMsg)
                }

            }
        });

        //请求放映时间段
        $.ajax({
            type: 'post', //默认get
            url: "/api/listShowTime.do",
            data: {id: id},
            async: true,   //是否异步（默认true：异步）
            dataType: "json",//定义服务器返回的数据类型
            success: function (data) {//data服务器返回的json字符串
                if (data.success) {
                    app.showTimeList = data.data;
                } else {
                    alert(data.errorMsg)
                }

            }
        });

    }
});





