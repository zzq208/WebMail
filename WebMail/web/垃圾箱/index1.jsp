<%--
  Created by IntelliJ IDEA.
  User: zhuzhaoqian
  Date: 2017/10/25
  Time: 00:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8"); %>
<html>
<head>
    <title>WebMail</title>
</head>
<body>
    <form action="../nav.jsp">
        <table>
            <tbody>
            <tr>
                <td>收件人</td>
                <td>
                    <div>
                        <input type="email ">
                    </div>
                </td>
            </tr>
            <tr>
                <td>主题</td>
                <td>
                    <div>
                        <input>
                    </div>
                </td>
            </tr>
            <tr>
                <td>正文</td>
                <td>
                    <div>
                        <textarea name=" " id="" cols="30" rows="10"></textarea>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <button>发送</button>
                </td>
                <td>
                    <button>存草稿</button>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</body>
</html>

