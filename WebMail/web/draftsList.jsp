<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/7
  Time: 16:06
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
    <title>Javamail邮箱草稿箱</title>

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
        <div class="modal-title"><strong>草稿箱</strong>(共<span id ="allDraftCount"></span>封)</div>
        <hr style='background-color:#999;height:1px;border:none;'>
        <div class="">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div class="btn-group" role="group" aria-label="...">
                        <button type="button" class="btn btn-default" onclick="deleteDraft()">删除草稿</button>
                    </div>
                </div>

                <!-- Table -->
                <table class="table table-hover">
                    <colgroup>
                        <col style="width:2%">
                        <col style="width:5%">
                        <col style="width:20%">
                        <col style="width:35%">
                        <col style="width:38%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th class="tt">No</th>
                        <th class="tt">收件人</th>
                        <th class="tt">主题</th>
                        <th class="tt">内容</th>
                    </tr>
                    </thead>
                    <tbody id="DraftsTbody" style="cursor: pointer">
                    </tbody>
                </table>
            </div>
        </div>
        <nav aria-label="...">
            <ul class="pager">
                <li><a href="#">上一页</a></li>
                <li><a href="#">下一页</a></li>
            </ul>
        </nav>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery.cookie.js"></script>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="toastr/js/toastr.js"></script>
<script src="js/Batch.js"></script>
<script src="js/myJs.js"></script>
</body>
</html>