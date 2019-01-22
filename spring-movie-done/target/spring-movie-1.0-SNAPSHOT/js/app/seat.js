var app = new Vue({
    el: '#app',
    data: {
        showInfo:{},

        movie: {},
        selectedSeats: [], //"1_1","1_2"
        unavailableSeats: [],
        number: 0,
        price: null
    },

    watch: {//侦听器（侦听unavailableSeats属性变化，即执行）
        // 如果 `question` 发生改变，这个函数就会运行
        //用来侦听座位是否被售出
        unavailableSeats: function (newValue, oldValue) { //newValue,改变之后的监听对象值，oldValue,改变之前的监听对象值

            $.each(newValue, function (index, value) {
                $("#" + value).removeClass("available").addClass("unavailable");//改成已售出
            })
        }
    },

    computed: {
        sum: function () {

            return this.number * this.showInfo.price;
        }
    },

    methods: {

        listSaledSeats : function(){
            var showTimeId = UrlParam.param("showTimeId");
            $.ajax({
                type: 'post', //默认get
                url: "/api/order/listSaledSeats.do",
                data: {showTimeId: showTimeId},
                async: true,   //是否异步（默认true：异步）
                dataType: "json",//定义服务器返回的数据类型
                success: function (data) {//data服务器返回的json字符串
                    if (data.success) {
                        // $.each(data.data, function (index, value) {
                        //     $("#" + value).removeClass("available").addClass("unavailable");//改成已售出
                        // })
                        app.unavailableSeats = data.data;
                    } else {
                        alert(data.errorMsg)
                    }

                }
            });
        },

        // 将座位号格式化为指定格式
        formatSeat: function (v) {
            var seat = v.split('_');
            return seat[0] + '排' + seat[1] + '座';
        },
        // 选座，同时改变选定座位的状态
        chooseSeat: function (event) {


            var currentTargetDom = event.currentTarget;
            //原生js方式
            // var id = currentTargetDom.getAttribute("id");
            // this.selectedSeats.push(id);//向数组尾部添加

            //jquery方式
            var jquery_currentTargetDom = $(currentTargetDom);
            var id = jquery_currentTargetDom.attr("id");

            //如果座位已经售出则不执行后面的逻辑
            if (this.unavailableSeats.includes(id)) {
                return;
            }

            //更改样式
            // if (this.selectedSeats.indexOf(id)===-1) {
            if (!this.selectedSeats.includes(id)) {
                jquery_currentTargetDom.removeClass("available").addClass("selected");
                this.selectedSeats.push(id);
                // this.number++;
            } else {
                jquery_currentTargetDom.removeClass("selected").addClass("available");
                this.selectedSeats.splice(this.selectedSeats.indexOf(id), 1);
                // this.number--;
            }
            this.number = this.selectedSeats.length;
        },
        //清空购物车
        resetBuyed: function () {
            $.each(this.selectedSeats, function (index, value) {
                $("#" + value).removeClass("selected").addClass("available");
            });
            this.selectedSeats.splice(0, this.selectedSeats.length);
            this.number = 0;
        },
        addOrder:function(){
            var showTimeId = UrlParam.param("showTimeId");
            var movieId=UrlParam.param("movieId");
            $.ajax({
                type: 'post', //默认get
                url: "/api/order/addOrder.do",
                data: {
                    movieId: movieId,//需要查询
                    showTimeId: showTimeId,
                    selectedSeats: this.selectedSeats
                },
                async: true,   //是否异步（默认true：异步）
                dataType: "json",//定义服务器返回的数据类型
                success: function (result) {//data服务器返回的json字符串
                    if (result.success) {
                        $("#app").html(result.data);
                    } else {
                        alert(result.errorMsg);
                        if(result.errorCode === 600){
                            //跳转到登录页面
                            window.location.href="/login.html";
                        }

                        //重新刷新
                        //从后台获取已经售出的座位
                        app.listSaledSeats();
                        //清空已选择座位
                        app.selectedSeats = [];


                    }

                }
            });

        },
        enter: function (event) {

            var currentTargetDom = event.currentTarget;
            var jquery_currentTargetDom = $(currentTargetDom);
            jquery_currentTargetDom.addClass("focused");

        },
        leave: function (event) {

            var currentTargetDom = event.currentTarget;
            var jquery_currentTargetDom = $(currentTargetDom);

            jquery_currentTargetDom.removeClass("focused");

        }
    },

    created: function () {
        //获取电影信息
        // var movieId=UrlParam.param("movieId");
        // $.ajax({
        //     type: 'post', //默认get
        //     url: "/api/getMovieInfo.do",   //请求的目标，从该处请求出所需要的json数据
        //     data: {id: movieId},    //向后端发请求时，携带的数据
        //     async: true,   //是否异步（默认true：异步）
        //     dataType: "json",//定义服务器返回的数据类型
        //     success: function (data) {//data服务器返回的json字符串
        //         if (data.success) {
        //             app.movie = data.data;
        //         } else {
        //             alert(data.errorMsg)
        //         }
        //
        //     }
        // });



        var showTimeId = UrlParam.param("showTimeId");
        $.ajax({
            type: 'post', //默认get
            url: "/api/ShowInfo.do",   //请求的目标，从该处请求出所需要的json数据
            data: {showTimeId: showTimeId},    //向后端发请求时，携带的数据
            async: true,   //是否异步（默认true：异步）
            dataType: "json",//定义服务器返回的数据类型
            success: function (data) {//data服务器返回的json字符串
                if (data.success) {
                    app.showInfo = data.data;
                } else {
                    alert(data.errorMsg)
                }

            }
        });



    },

    mounted: function () {
        //从后台获取已经售出的座位
        this.listSaledSeats();

        // $.each(this.unavailableSeats,function(index,value) {
        //     $("#"+value).removeClass("available").addClass("unavailable");
        // })

    },


//filters: {
//     formatSeatFilter: function (value) {
//         if (!value) return ''
//         var strs = value.split('_');//[1,1]
//         return strs[0] + '排' + strs[1] + '座';
//     }
// }
//
});