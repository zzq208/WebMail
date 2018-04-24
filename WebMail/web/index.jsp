<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/8
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
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
    <link rel="stylesheet" type="text/css" href="css/mainFrame.css">

</head>
<style>
    .tt {
        background: url(img/line.png) 0 0 repeat-y;
        background-color: rgb(242, 244, 246);
    }
</style>
<body>
<%
    HttpSession a = request.getSession();
    if(a.getAttribute("loginer")==null){
        request.getRequestDispatcher("login.jsp").forward(request,response);
    }
%>
<jsp:include page="nav.jsp" flush="true"></jsp:include>
<div class="container-fluid">
    <jsp:include page="sidebar.jsp" flush="true"></jsp:include>
    <div id = "mainFrame" class="col-xs-12 col-sm-11 col-md-11 container">
        <label style="font-size:16px;">欢迎使用</label>
        <p style="margin-top:5px; margin-bottom:10px"> 今天是2018年4月18日，天气晴，27°，适宜出游！ </p>
        <p style="margin-bottom:150px"> <a class="btn btn-lg btn-primary" href="mailList.jsp" role="button">进入收件箱</a> </p>
        <div class="row">
            <div class="col-sm-3 col-md-3 col-xs-3"> <a href="#" class="thumbnail"> <img src="img/广告.png"
                                                                                         alt="通用的占位符缩略图"> </a> </div>
            <div class="col-sm-3 col-md-3 col-xs-3"> <a href="#" class="thumbnail"> <img src="img/广告.png"
                                                                                         alt="通用的占位符缩略图"> </a> </div>
            <div class="col-sm-3 col-md-3 col-xs-3"> <a href="#" class="thumbnail"> <img src="img/广告.png"
                                                                                         alt="通用的占位符缩略图"> </a> </div>
            <div class="col-sm-3 col-md-3 col-xs-3"> <a href="#" class="thumbnail"> <img src="img/广告.png"
                                                                                         alt="通用的占位符缩略图"> </a> </div>
        </div>
    </div>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery.cookie.js"></script>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="toastr/js/toastr.js"></script>
<script src="js/myJs.js"></script>
</body>
</html>
