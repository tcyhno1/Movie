function login() {

    $.ajax({
        type: 'post', //默认get
        url: "/api/user/login.do",   //请求的目标，从该处请求出所需要的json数据
        data: $("#loginForm").serialize(),    //向后端发请求时，携带的数据
        async: true,   //是否异步（默认true：异步）
        dataType: "json",//定义服务器返回的数据类型
        success: function (result) {//data服务器返回的json字符串
            if (result.success) {
                window.location.href="/"
            } else {
                alert(result.errorMsg)
            }

        }
    });

}