<%--
  Created by IntelliJ IDEA.
  User: zzq
  Date: 2018/4/15
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bootstrap 实例 - 模态框（Modal）插件事件</title>
    <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/jquery.min.js"></script>
    <script src="/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

<h2>模态框（Modal）插件事件</h2>

<!-- 按钮触发模态框 -->
<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
    开始演示模态框
</button>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    模态框（Modal）标题
                </h4>
            </div>
            <div class="modal-body">
                点击关闭按钮检查事件功能。
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                    关闭
                </button>
                <button type="button" class="btn btn-primary">
                    提交更改
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div contenteditable="true" accesskey="q" style="padding: 2px 4px 0px;"><div>撒旦发顺丰撒旦发射点发撒法</div></>
<script>
   // $(function () { $('#myModal').modal('hide')});
</script>
<script>
    $(function () { $('#myModal').on('hide.bs.modal', function () {
        alert('嘿，我听说您喜欢模态框...');})
    });
</script>

</body>
</html>