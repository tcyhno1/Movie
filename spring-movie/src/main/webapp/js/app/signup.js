var app = new Vue({
    el: '#app',
    data: {},
    methods: {
        sendCode: function () {

            $("#btn_sendcode").attr('disabled', 'disabled').html('发送中');

            var email = $("#email").val();
            if (!email) {
                alert("请输入邮箱");
                return;
            }

            $.ajax({
                type: 'post', //默认get
                url: "/api/user/sendCode.do",
                data: {email: email},
                async: true,   //是否异步（默认true：异步）
                dataType: "json",//定义服务器返回的数据类型
                success: function (data) {//data服务器返回的json字符串
                    if (data.success) {
                        app.codeCount(10);
                    } else {
                        alert(data.errorMsg)
                    }

                }
            });
        },
        codeCount: function (c) {
            if (c === 0) {
                $("#btn_sendcode").removeAttr('disabled').html('发送验证码');
                return;
            }

            $("#btn_sendcode").html(c--);

            setTimeout(function () {
                app.codeCount(c)
            }, 1000);
        },
        signUp: function () {

            var email = $("#email").val();
            if (!email) {
                alert("请输入邮箱");
                return;
            }
            var password = $("#password").val();
            if (!password) {
                alert("请输入密码");
                return;
            }
            $.ajax({
                type: 'post', //默认get
                url: "/api/user/signUp.do",
                data: $("#form1").serialize(),//form表单序列化
                async: true,   //是否异步（默认true：异步）
                dataType: "json",//定义服务器返回的数据类型
                success: function (result) {//data服务器返回的json字符串
                    if (result.success) {
                        alert("注册成功");
                    } else {
                        alert(result.errorMsg)
                        if (result.errorCode === 601) {
                            document.getElementById("validateCode").value = "";
                            // $("#validateCode").val("");
                        }
                    }

                }
            });
        }

    }

});