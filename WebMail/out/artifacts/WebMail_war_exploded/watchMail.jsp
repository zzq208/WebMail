<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/7
  Time: 15:42
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
<%
    HttpSession a = request.getSession();
    if(a.getAttribute("loginer")==null){
        request.getRequestDispatcher("login.jsp").forward(request,response);
    }
%>
<body>
<jsp:include page="nav.jsp" flush="true"></jsp:include>
<div class="container-fluid" >
    <jsp:include page="sidebar.jsp" flush="true"></jsp:include>
    <div id = "mainFrame" class="col-xs-12 col-sm-11 col-md-11 container">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="btn-group" role="group" aria-label="...">
                        <button type="button" class="btn btn-default" onclick="window.history.back()">返回</button>
                        <button type="button" class="btn btn-default">删除</button>
                    </div>
                </div>
                <div class="panel-body head">
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                        <label style="padding-bottom:10px" id="watchSubject"></label>
                        <tr>
                            <td>
                                <span class="tcolor"> 发件人： </span>
                                <span class="tcolor" id="watchFrom"></span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span class="tcolor">时&nbsp;&nbsp; 间：</span>
                                <span class="tcolor" id="watchTime"></span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span class="tcolor"> 收件人： </span>
                                <span class="tcolor" id="watchReceive"></span>
                            </td>
                        </tr>
                        <tr style="display:none" id="trAttachment">
                            <td>
                                <span class="tcolor"> 附&nbsp;&nbsp; 件： </span>
                                <span class="tcolor" id="attachment"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="content" id = "displayContent"></div>
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