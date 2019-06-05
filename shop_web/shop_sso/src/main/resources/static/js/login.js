$(function(){
    $.ajax({
        method: "GET",
        url: "http://localhost:8084/sso/islogin",
        success: function(data){
            var html = "";
            if(data){
                //说明已经登录
                html = data.nickname + "您好，欢迎来到<b>ShopCZ商城</b>  <a href='http://localhost:8084/sso/logout'>注销</a>";
            } else {
                //说明未登录
                html = "[<a onclick='login();'>登录</a>][<a href=\"http://localhost:8084/sso/toregister\">注册</a>]";
            }

            $("#pid").html(html);
        },
        dataType: "jsonp",
        jsonpCallback: "m"
    });
});

function login(){

    //获得当前的请求地址
    var returnUrl = location.href;
    //给url做一个编码
    returnUrl = encodeURIComponent(returnUrl);

    //跳转到登录页
    location.href = "http://localhost:8084/sso/tologin?returnUrl=" + returnUrl;
}
