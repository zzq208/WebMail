<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Javamail邮箱</title>
    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link href="toastr/css/toastr.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="css/myCss.css">

</head>
<body>
<%
    HttpSession a = request.getSession();
    if(a.getAttribute("loginer")!=null){
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
%>
<div class="all">
    <div class="second">
    <form action="" method="post" id="loginForm">
        <input type="text" placeholder="用户名" class="username" name="username" id="username"/><br />
        <input type="password"  placeholder="密码"  class="password" name="password" id="password"/>
        <span class="tnext">下次自动登录</span>
        <input type="checkbox" class="bnext" id="remember"/>
        <input type="button" value="登录"  class="ok" onclick="checkID()"/>
    </form>
    </div>
</div>

<script>
    window.onload = function(){
        var oForm = document.getElementById('loginForm');
        var oUser = document.getElementById('username');
        var oPswd = document.getElementById('password');
        var oRemember = document.getElementById('remember');
        //页面初始化时，如果帐号密码cookie存在则填充
        if(getCookie('username') && getCookie('password')){
            oUser.value = getCookie('username');
            oPswd.value = getCookie('password');
            oRemember.checked = true;
        }
        //复选框勾选状态发生改变时，如果未勾选则清除cookie
        oRemember.onchange = function(){
            if(!this.checked){
                delCookie('username');
                delCookie('password');
            }
        };
        //表单提交事件触发时，如果复选框是勾选状态则保存cookie
        oForm.onclick = function(){
            if(oRemember.checked){
                setCookie('username',oUser.value,7); //保存帐号到cookie，有效期7天
                setCookie('password',oPswd.value,7); //保存密码到cookie，有效期7天
            }
        };
    };
    //设置cookie
    function setCookie(name,value,day){
        var date = new Date();
        date.setDate(date.getDate() + day);
        document.cookie = name + '=' + value + ';expires='+ date;
    }
    //获取cookie
    function getCookie(name){
        var reg = RegExp(name+'=([^;]+)');
        var arr = document.cookie.match(reg);
        if(arr){
            return arr[1];
        }else{
            return '';
        }
    }
    //删除cookie
    function delCookie(name){
        setCookie(name,null,-1);
    }
</script>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery.cookie.js"></script>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="toastr/js/toastr.js"></script>
<script src="js/myJs.js"></script>
</body>
</html>