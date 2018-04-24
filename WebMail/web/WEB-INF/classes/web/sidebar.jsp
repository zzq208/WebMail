<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/7
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8"); %>
<style>
    #side{
        position: absolute;
        height:96.13%;
        width:8.33%;
        background-color:#f5f5f5;
    }
</style>
<div id="side"></div>
<div class="col-md-1 col-sm-1 hidden-xs sidebar" style="padding: 20px;">
    <ul class="nav nav-pills nav-stacked">
        <li><a href="writeMail.jsp" onclick="stopAjax()"><strong>写信</strong></a></li>
        <li><a href="mailList.jsp" onclick="clickMailList()"><strong>收信</strong></a></li>
    </ul>
    <hr style='background-color:#999;height:1px;border:none;'>
    <ul class="nav nav-pills nav-stacked">
        <li><a href="mailList.jsp">收信箱</a></li>
        <li><a href="draftsList.jsp" onclick="stopAjax()">草稿箱</a></li>
    </ul>
</div>