<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/4
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="javax.sql.*,com.zzq.dao.DB"
         contentType="text/html;charset=utf-8" %>
<jsp:useBean id="user" class="com.zzq.valuebean.UserBean">
    <jsp:setProperty name="user" property="*"></jsp:setProperty>
</jsp:useBean>
<%
    DB db = new DB();
    if(db.userLogin(user)){
        String username = request.getParameter("username");
        session.setAttribute("username",username);
        response.sendRedirect("index.jsp");
}
    else{
%>
        <div class="alert alert-danger" role="alert">输入的用户名或密码错误！</div>
<%
    }
%>
