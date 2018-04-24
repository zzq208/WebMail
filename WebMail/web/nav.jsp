<%--
  Created by IntelliJ IDEA.
  User: zhuzhaoqianzhu
  Date: 2017/12/4
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%request.setCharacterEncoding("UTF-8"); %>
<%--<div class="modal fade" id="confirm" tabindex="1" role="dialog" aria-hidden="true">--%>
    <%--<div class="modal-dialog" style="width:250px">--%>
        <%--<div class="modal-content">--%>
            <%--<div class="modal-body" style="text-align: center">--%>
                <%--确认删除该账户吗？--%>
            <%--</div>--%>
            <%--<div class="modal-footer" style="text-align: center">--%>
                <%--<button type="button" class="btn btn-primary" id="delete" onclick="">确认</button>--%>
                <%--<button type="button" class="btn" data-dismiss="modal" hidden="hidden" >取消</button>--%>
            <%--</div>--%>
            <%--<!-- /.modal-content -->--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="navbar-header"> <a class="navbar-brand" href="index.jsp">JavaMail邮箱</a> </div>
		<ul class="nav navbar-nav navbar-left">
			<li><a style="color:#FFF"><strong><span id="nick"></span></strong>&lt;<span id="mail"></span>&gt;</a></li>
			<li><a href="#" data-toggle="modal" data-target="#setting">设置</a></li>
			<li><a href="#" id="signOut">退出</a></li>
		</ul>
	</nav>
</div>

<!-- 模态框1 -->
<div class="modal fade" id="setting" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">更改邮箱账户</h4>
			</div>
			<div class="modal-body">
				<div class="list-group" id="nickNameList" style="cursor: pointer">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#changeMail" id="addNickName" onclick="addMailAccount()">新增</button>
				<button type="button" class="btn btn-info" data-toggle="modal" data-target="#changeMail" id="editInfo" onclick="editInfo()">编辑</button>
				<button type="button" class="btn btn-primary" onclick="isUse()">设为当前</button>
			</div>
		</div>
		<!-- /.modal-content --> 
	</div>
	<!-- /.modal --> 
</div>
<!-- 模态框2 -->
<div class="modal fade" id="changeMail" tabindex="0" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="" method="post">
                <div class="modal-body">
					<div class="form-group">
						<label for="SMTPServer">发件人昵称</label>
						<input type="text" name="nickName" class="form-control" id="nickName" placeholder="发件人昵称">
					</div>
                    <div class="form-group">
						<label for="SMTPServer">SMTP服务器</label>
						<input type="text" name="SMTPServer" class="form-control" id="SMTPServer" placeholder="SMTP服务器">
					</div>
					<div class="form-group">
						<label for="POP3Server">POP3服务器</label>
						<input type="text" name="POP3Server" class="form-control" id="POP3Server" placeholder="POP3服务器">
					</div>
					<div class="form-group">
						<label for="mailUsername">账号名</label>
						<input type="text" name="mailUsername" class="form-control" id="mailUsername" placeholder="账号名">
					</div>
					<div class="form-group">
						<label for="mailPassword">密码</label>
						<input type="password" name="mailPassword" class="form-control" id="mailPassword" placeholder="密码">
					</div>
			    </div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="save" onclick="updateMailAccount()">保存</button>
				<button type="button" class="btn" data-dismiss="modal" hidden="hidden" id="cancel">取消</button>
			</div>
			</form>
		<!-- /.modal-content -->
	    </div>
    </div>
</div>
