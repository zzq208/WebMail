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
    <div id="mainFrame" class="col-xs-12 col-sm-11 col-md-11 container">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label for="recipient" class="col-md-1 control-label">收件人：</label>
                <div class="col-md-10">
                    <input type="email" class="form-control" id="recipient" name="recipient" placeholder="Email">
                </div>
            </div>
            <div class="form-group">
                <label for="subject" class="col-md-1 control-label">主题：</label>
                <div class="col-md-10">
                    <input type="text" class="form-control" id="subject" name="subject" placeholder="">
                </div>
            </div>
            <div class="form-group">
                <label for="attachment" class="col-md-1 control-label">上传附件:</label>
                <div class="col-md-10">
                    <input type="file" id="attachment" name="attachment">
                    <p class="help-block">附件大小最好不要超过2M</p>
                </div>
            </div>
            <div class="form-group">
                <label for="content" class="col-md-1 control-label">正文：</label>
                <div class="col-md-10">
                    <textarea style="height: 55%;overflow: auto;overflow-x: hidden " type="textarea" class="form-control" row="10" id="content" name="content"></textarea>
                </div>
            </div>
            <div class="form-group">
                <label for="addresser" class="col-md-1 control-label">发件人：</label>
                <div class="col-md-10" id = "addresser">
                </div>
            </div>
            <div class="form-group">
                <label class = "col-md-1 control-label"></label>
                <div class="col-md-10">
                    <button id = "sand" type="button" class="btn btn-default" onclick="sendMail()">发送</button>
                    <button id = "toDraft" type="button" class="btn btn-default" onclick="saveToDraftBox()">存到草稿箱</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/jquery.cookie.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="toastr/js/toastr.js"></script>
<script src="js/myJs.js"></script>
</body>
</html>

