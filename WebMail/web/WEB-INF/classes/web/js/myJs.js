//用户正在使用的邮箱账户，是一个json对象
var isUseMailAccount;
var allMailAccount;
var allDrafts;
var mailList;//保存邮件列表json对象
var page=1;//页数
var lastpage=0;//最后一页页码
var count=0;//每页邮件个数
var allCount=0;//所有邮件个数
var lastMainFrame="";//上一个页面
var whichMailAccount=-1;//确定哪个MailAccount被点击
var op=-1;//对账户操作，op为0是更新已有账户；为1是添加新的账户；2是删除账户；3是设为默认账户；
var op1=-1;//对草稿箱操作，0是更新，1是添加，2是删除
var getmaillistAjax = null;
var objURL;
$(document).ready(function() {
    //阻止事件冒泡
    $("input[type='checkbox']").click(function(e){
        e.stopPropagation();
    });
    //监听跳页按钮
    if(page==1){
        $("#previous").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
        $("#previous").attr('disable',true);
    }
    //使警告框可以被关闭
    $(function(){
        $(".close").click(function(){
            $("#a").alert();
        });
    });
    //使弹出模态框居中
    $('#confirm').on('show.bs.modal', centerModals('#confirm'));
    function centerModals(selector) {
        $(selector).each(function(i) {
            var $clone = $(this).clone().css('display','block').appendTo('body');
            var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
            top = top > 0 ? top : 0;
            $clone.remove();
            $(this).find('.modal-content').css("margin-top", top);
        });
    }
    $('#setting').on('show.bs.modal',function () {
        if(allMailAccount==""){
            $("#editInfo").attr("disabled","disabled");
        }
        else{
            $("#editInfo").removeAttr("disabled","disabled");
        }
    });

    //退出操作
    $("#signOut").on('click',function () {
        $.post("LoginServlet",{action:"logout"}, function () {
            window.location="login.jsp";
        })
    });
    getMailAccountLists();
    var title = document.title.valueOf();
    console.log(title);
    if(title=="Javamail邮箱收件箱"){
        getMailList();
    }
    if(title=="Javamail邮箱草稿箱"){
        getDraftList();
    }
});
function stopAjax() {
    getmaillistAjax.abort();
}
//发送邮件
function sendMail() {
    var recipient = $("#recipient").val();
    var subject = $("#subject").val();
    var content = $("#content").val();
    var attachment = $("#attachment").val();
// console.log(filename);
// console.log($("#attachment")[0].files[0]);
// formData.append("filename",filename);
//     $.ajax({
//         url: "SendMail",
//         type: "POST",
//         data: formData,
// // 告诉jQuery不要去处理发送的数据
//         processData : false,
// // 告诉jQuery不要去设置Content-Type请求头
//         contentType : false,
//         beforeSend:function(){
//             console.log("正在进行，请稍候");
//         },
//         success : function(responseStr) {
//             if(responseStr.status===0){
//                 console.log("成功"+responseStr);
//             }else{
//                 console.log("失败");
//             }
//         },
//         error : function(responseStr) {
//             console.log("error");
//         }
//     });
    $.ajax({
        type:"post",
        url:"SendMail",
        data:{
            recipient:recipient,
            subject:subject,
            content:content,
            attachment:attachment
        },
        async:true,
        success:function (data) {
            if(data=="1") {
                toastr.success("发送成功！")
            }
            else
                toastr.erro("发送失败！")
        }
    });
}
//上一页
function previous() {
    if(page>1){
        if(page==2) {
            $("#previous").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
            $("#previous").attr('disable',true);
        }
        page -= 1;
        $("#tbody").empty();
        getMailList();
        $("#next").removeAttr('style');
        $("#next").removeAttr('disable');
    }
    else{
        $("#previous").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
        $("#previous").attr('disable',true);
    }
}
//下一页
function next() {
    if(page!=lastpage){
        if(page==lastpage-1){
            $("#next").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
            $("#next").attr('disable',true);
        }
        page += 1;
        $("#previous").removeAttr('style');
        $("#previous").removeAttr('disable');
        $("#tbody").empty();
        getMailList();
    }
    else {
        $("#next").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
        $("#next").attr('disable',true);
    }
}
//上一封
function previousMail() {
    loadMail()
}
//下一封
function nextMail() {

}

//得到邮箱账户列表数据
function getMailAccountLists() {
    $.ajax({
        type:"post",
        url:"GetMailAccount",
        data:{
        },
        async:false,
        success:function (data) {
            console.log("成功得到");
            //如果成功执行，载入邮件列表，并且在loadMailAccountLists中顺便得到了用户正在使用的邮箱账户
            loadMailAccountLists(data);
        }
    });
}
//点击收信按钮
function clickMailList() {
    $("#tbody").empty();
        page=1;
        previous();
        freshMailList();
}
//点击收信刷新
function freshMailList(){
    $.ajax({
        type:"post",
        url:"GetMailList",
        data:{
            host:isUseMailAccount.pop3Server,
            maiUsername:isUseMailAccount.mailUsername,
            maiPassword:isUseMailAccount.mailPassword,
            page:"1"
        },
        async:true,
        success:function (data) {
            console.log(data);
            loadMailList(data);
        }
    });
}
//得到邮件列表
function getMailList() {
    getmaillistAjax=$.ajax({
        type:"post",
        url:"GetMailList",
        data:{
            host:isUseMailAccount.pop3Server,
            maiUsername:isUseMailAccount.mailUsername,
            maiPassword:isUseMailAccount.mailPassword,
            page:page
        },
        async:false,
        success:function (data) {
            console.log(data);
            loadMailList(data);
        }
    });
}
//载入邮件列表
function loadMailList(data) {
    var dataObj = JSON.parse(data);
    var num = 0;
    //储存邮件列表
    mailList = dataObj;
    //得到最后一页页码
    lastpage = dataObj[0].page;
    //所有邮件个数
    allCount = dataObj[0].count;
    $("#allCount").html(allCount);
    console.log(dataObj);
    console.log("长度:"+dataObj.length);
    for(var i = 1;i<dataObj.length;i++){
        if(page!=lastpage){
            count = dataObj.length-1;
            num = (page-1)*count+i;
        }
        else{
            num = (page-1)*count+i;
        }
        var subject = dataObj[i].subject;
        var from = dataObj[i].from;
        var sendDate = dataObj[i].sendDate;
        var recipients = dataObj[i].recipients;
        var isAttachment = dataObj[i].isAttachment;
        $("#tbody").append('<tr><td><input type="checkbox" name="mailSelect" value="'+num+'"></td>\n' +
            '                        <th scope="row">'+num+'</th>\n' +
            '                        <td onclick="loadMail(&quot;'+from+'&quot;,&quot;'+subject+'&quot;,&quot;'+sendDate+'&quot;,&quot;'+recipients+'&quot;,&quot;'+isAttachment+'&quot;,&quot;'+num+'&quot;)">'+dataObj[i].from+'</td>\n' +
            '                        <td onclick="loadMail(&quot;'+from+'&quot;,&quot;'+subject+'&quot;,&quot;'+sendDate+'&quot;,&quot;'+recipients+'&quot;,&quot;'+isAttachment+'&quot;,&quot;'+num+'&quot;)">'+dataObj[i].subject+'</td>\n' +
            '                        <td onclick="loadMail(&quot;'+from+'&quot;,&quot;'+subject+'&quot;,&quot;'+sendDate+'&quot;,&quot;'+recipients+'&quot;,&quot;'+isAttachment+'&quot;,&quot;'+num+'&quot;)">'+SimpleTime(dataObj[i].sendDate)+'</td>\n' +
            '                    </tr>');
        lastMainFrame = $("#mainFrame").html();

    }
}
//获取被批量选中的邮件
function fun(){
    var obj = document.getElementsByName("mailSelect");
    var checkedNum = new Array() ;
    var num = 0;
    for(var i=0 ;i<obj.length;i++){
        if(obj[i].checked){
            checkedNum[num] = obj[i].value;
            num++;
        }
    }
    return checkedNum;
}
//删除邮件
function deleteMail(){
    var checkedNum = fun();
    checkedNum = JSON.stringify(checkedNum);
    $.ajax({
        type:"post",
        url:"DeleteMail",
        data:{
            checkedNum:checkedNum
        },
        async:true,
        success:function (data) {
            if(data=="1") {
                successed();
                $("#tbody").empty();
                getMailList();
            }
            else
                failed();
        }
    });

}
//载入邮件
function loadMail(from,subject,sendDate,recipients,isAttachment,num) {
    //输出内容
    $("#mainFrame").html('<div class="panel panel-default">\n' +
        '                <!-- Default panel contents -->\n' +
        '                <div class="panel-heading">\n' +
        '                    <div class="btn-group" role="group" aria-label="...">\n' +
        '                        <button type="button" class="btn btn-default" onclick="goBack(lastMainFrame)">返回</button>\n' +
        '                        <button type="button" class="btn btn-default">回复</button>\n' +
        '                        <button type="button" class="btn btn-default">转发</button>\n' +
        '                        <button type="button" class="btn btn-default">删除</button>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="panel-body head">\n' +
        '                    <table border="0" cellspacing="0" cellpadding="0">\n' +
        '                        <tbody>\n' +
        '                        <label style="padding-bottom:10px" id="watchSubject">'+subject+'</label>\n' +
        '                        <tr>\n' +
        '                            <td>\n' +
        '                                <span class="tcolor"> 发件人： </span>\n' +
        '                                <span class="tcolor" id="watchFrom"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>\n' +
        '                                <span class="tcolor">时&nbsp;&nbsp; 间：</span>\n' +
        '                                <span class="tcolor" id="watchTime">'+sendDate+'</span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr>\n' +
        '                            <td>\n' +
        '                                <span class="tcolor"> 收件人： </span>\n' +
        '                                <span class="tcolor" id="watchReceive"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        <tr style="display:none" id="trAttachment">\n' +
        '                            <td>\n' +
        '                                <span class="tcolor"> 附&nbsp;&nbsp; 件： </span>\n' +
        '                                <span class="tcolor" id="attachment"></span>\n' +
        '                            </td>\n' +
        '                        </tr>\n' +
        '                        </tbody>\n' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="content" id = "displayContent">信件已收到，我会尽快回复给您！</div>\n' +
        '            </div>\n' +
        '            <nav aria-label="...">\n' +
        '                <ul class="pager">\n' +
        '                    <li><a href="#">上一封</a></li>\n' +
        '                    <li><a href="#">下一封</a></li>\n' +
        '                </ul>\n' +
        '            </nav>');
    var nickname;
    var address;
    var arr = from.split(" ");
    var rex = new RegExp("\<|\>","g");
    arr[1] = arr[1].replace(rex,"");
    nickname = arr[0];
    address = arr[1];
    //如果存在多位收件人需要处理字符
    var arr2 = recipients.split(",");
    console.log("arr2:"+arr2);
    $("#watchSubject").html(subject);
    $("#watchFrom").html('<b style="color:#5fa207">'+nickname+' </b>&lt;'+address+'&gt;');
    $("#watchTime").html(sendDate);
    for(var i in arr2){
        var name;
        var addr;
        var arr3 = arr2[i].split(" ");
        if(arr3[1]!=null){
            name = arr[0];
            addr = arr3[1].replace(rex,"");
        }
        else {
            var arr4 = arr3[0].split("@");
            name = arr4[0];
            addr = arr3[0];
        }
        $("#watchReceive").append('<span style="color: black">'+name+'</span>&nbsp;&lt;'+addr+'&gt;');
        if(i!=arr2.length-1)
            $("#watchReceive").append(';&nbsp;');
    }
    //发送请求邮件内容的Ajax请求
    //console.log(num);
    $.ajax({
        type:"post",
        url:"GetMailContent",
        data:{
            num:num-1,
            page:page,
            once:count
        },
        async:false,
        success:function (data) {
            loadContent(data);
        }
    });
    if(isAttachment == 1){
        getAttachment(num);
    }
}
//请求附件并生成附件链接
function getAttachment(num) {
    $.ajax({
        type:"post",
        url:"GetMailAttachment",
        data:{
        },
        async:true,
        success:function (data) {
            console.log(data);
            if(data=="noneAttachment"){
                //什么也不做
            }
            else{
                loadAttachment(data,num);
            }
        }
    });
    function loadAttachment(data,num) {
        var dataObj = JSON.parse(data);
        $("#trAttachment").attr('style','display:block');
        $("#attachment").html(dataObj.length+'<br/>');
        for(i=0;i<dataObj.length;i++){
            $("#attachment").append('<a href="HandleAttach?num='+num+'\&\&attachmentNum='+dataObj[i].attachmentNum+'\&\&fileName='+dataObj[i].fileName+'">'+dataObj[i].fileName+'</a><br/>');
        }
    }
}
//返回上一个页面
function goBack(lasthtml) {
    //重新载入上个页面内容
    $("#mainFrame").html(lasthtml);
}
//显示邮件文本内容
function loadContent(data) {
    $("#displayContent").html(data);
}

//js方法获取url参数，返回对象
function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=decodeURIComponent(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

//简化时间
function SimpleTime(Time) {
    Time = Time.substr(0,11);
    return Time;
}

//点击激活,获取账户id
$("#nickNameList").on("click","a",function () {
    //console.log($("a[class='list-group-item list-group-item-action active']").html());
    //如果是正在使用账户的列表项不变css
    if($(this).is($("a[class='list-group-item list-group-item-action active']"))){
        whichMailAccount = $(this).index();
        $("div#nickNameList a").not(this).removeAttr("style");
    }else{
        $(this).attr("style","background-color:#EEEEEE");
        $("div#nickNameList a").not(this).removeAttr("style");
        whichMailAccount = $(this).index();
    }
});

//监听删除按钮被点击，执行删除账户操作
$(document).on('click','.close.pull-right',function () {
    whichMailAccount = $(this).parent().index();
    op=2;
    updateMailAccount();
    if(allMailAccount==""){
        $("#editInfo").attr("disabled","disabled");
    }
});
//隐藏模态框1时擦出痕迹
$(function () {
    $("#setting").on("hide.bs.modal",function () {
        $("div#nickNameList a").removeAttr("style");
        whichMailAccount=-1;
    });
});
//载入邮箱账户列表
function loadMailAccountLists(data) {
    var dataObj = JSON.parse(data);
    allMailAccount = dataObj;
    console.log(dataObj);
    for (var i=0;i<dataObj.length;i++){
        $("#nickNameList").append("<a type=\"button\" class=\"list-group-item list-group-item-action\">"+dataObj[i].nickname+"&lt;"+dataObj[i].mailUsername+"&gt;<button class=\"close pull-right\" aria-hidden=\"true\">&times;</button></a>");
        if(dataObj[i].is_use==1){
            //设置class为active
            $("#nickNameList a:eq("+i+")").attr("class","list-group-item list-group-item-action active");
            $("#nickNameList a:eq("+i+") button").remove();
            //把用户当前正在使用的账户记录下来
            isUseMailAccount=dataObj[i];
            console.log("use2 "+isUseMailAccount);
            $("#nick" ).empty();
            $("#mail" ).empty();
            $("#nick").append(isUseMailAccount.nickname);
            $("#mail").append(isUseMailAccount.mailUsername);
        }
    }
    $("#addresser").html(isUseMailAccount.nickname+"<"+isUseMailAccount.mailUsername+">");
}
//默认模态框2显示正在使用账户的数据
$(function () {
    $("#changeMail").on("hide.bs.modal",function () {
        console.log("CCC"+$("a[class='list-group-item list-group-item-action active']").index());
        if(whichMailAccount==-1){
            whichMailAccount = $("a[class='list-group-item list-group-item-action active']").index();
        }
    });
});
//编辑邮箱账户
function editInfo() {
    if(whichMailAccount==-1){
        whichMailAccount = $("a[class='list-group-item list-group-item-action active']").index();
    }
    //点击模态框1的编辑传递值
    $("#nickName").val(allMailAccount[whichMailAccount].nickname);
    $("#mailUsername").val(allMailAccount[whichMailAccount].mailUsername);
    $("#mailPassword").val(allMailAccount[whichMailAccount].mailPassword);
    $("#SMTPServer").val(allMailAccount[whichMailAccount].smtpserver);
    $("#POP3Server").val(allMailAccount[whichMailAccount].pop3Server);
    op=0;
}
//添加邮件账户
function addMailAccount() {
    //清空被填充的值
    $("#nickName").val("");
    $("#mailUsername").val("");
    $("#mailPassword").val("");
    $("#SMTPServer").val("");
    $("#POP3Server").val("");
    op=1;
}
//设为默认账户
function isUse() {
    op=3;
    updateMailAccount();
    $('#setting').modal('hide');
}
//防止用户名为空检测
function validateLogin() {
    var username = $("#username").val();
    username = username.replace(/\s+/g,"");
    var password = $("#password").val();
    password = password.replace(/\s+/g,"");
    var message = "";
    if(username==""){
        message += "用户名不得为空！"
    }
    if(password==""){;
        message +="密码不得为空！";
    }
    if(username!=""&&password!="")
        message = "用户名或密码错误！"
    return message;
}
//登陆检测
function checkID() {
    $.ajax({
        type:"post",
        url:"CheckID",
        data:{
            username:$("#username").val(),
            password:$("#password").val()
        },
        success:function (data) {
            if(data=="right"){
                alert("登陆成功");
                window.location="index.jsp";
            }
            else{
                alert(validateLogin());
            }
        }
    });

}
//更新邮箱账户展示列表
function updateMailAccount() {
    var id="";
    console.log(whichMailAccount);
    console.log(op);
    if(op==1){
        id = "";
    }else {
        id = allMailAccount[whichMailAccount].id;
    }
    $.ajax({
        type:"post",
        url:"UpdateMailAccount",
        data:{
            op:op,
            id:id,
            nickName:$("#nickName").val(),
            SMTPServer:$("#SMTPServer").val(),
            POP3Server:$("#POP3Server").val(),
            mailUsername:$("#mailUsername").val(),
            mailPassword:$("#mailPassword").val()
        },
        async:false,
        success:function (data) {
            if(data=="1") {
                alert("操作成功");
                $("#changeMail").modal('hide');
                $("#nickNameList").empty();
                window.location="index.jsp";
                $("#editInfo").removeAttr("disabled","disabled");
            }
            else
                alert("操作失败");
        }
    });
}

//设置弹出提示框的位置
toastr.options.positionClass = 'toast-center-center';
toastr.options.timeout = 10;

//反馈用户执行结果的提示框
function successed() {
    $('#confirm').modal('hide');
    toastr.success('删除成功！');

}
function failed() {
    $('#confirm').modal('hide');
    toastr.error('删除失败！');
}
//load草稿箱内容

function load(){

}
//存草稿箱按钮
function saveToDraftBox() {
    op1=1;
    UpdateDraft();
    setTimeout(function () { window.location = "index.jsp"; },3000);

}
//删除草稿
function deleteDraft() {
    op1=2;
    var checkedNum = fun1();
    checkedNum = JSON.stringify(checkedNum);
    $.ajax({
        type:"post",
        url:"UpdateDraftBox",
        data:{
            op1:op1,
            checkedNum:checkedNum
        },
        async:true,
        success:function (data) {
            if(data=="1") {
                successed();
                $("#DraftsTbody").empty();
                getDraftList();
            }
            else
                failed();
        }
    });
}
//更新草稿箱
function upDraft() {
    op1=3;
    UpdateDraft();
    window.history.back();
}
function getDraftList() {
    $.ajax({
        type:"post",
        url:"GetDrafts",
        data:{
        },
        async:false,
        success:function (data) {
            console.log("成功得到");
            //如果成功执行，载入邮件列表，并且在loadMailAccountLists中顺便得到了用户正在使用的邮箱账户
            loadDraftList(data);
        }
    });
}
function loadDraftList(data) {
    var dataObj = JSON.parse(data);
    allDrafts = dataObj;
    console.log(dataObj);
    $("#allDraftCount").html(dataObj.length);
    console.log("长度:" + dataObj.length);
    var num=0;
    for (var i = 0; i < dataObj.length; i++) {
        var subject = dataObj[i].subject;
        var recipient = dataObj[i].recipients;
        var content = dataObj[i].content;
        num++;
        $("#DraftsTbody").append('<tr><td><input type="checkbox" name="DraftsSelect" value="' + num + '"></td>\n' +
            '                        <th scope="row">' + num + '</th>\n' +
            '                        <td onclick="loadDraft(&quot;' + recipient + '&quot;,&quot;' + subject + '&quot;,&quot;' + content + '&quot;)">' + recipient + '</td>\n' +
            '                        <td onclick="loadDraft(&quot;' + recipient + '&quot;,&quot;' + subject + '&quot;,&quot;' + content + '&quot;)">' + subject + '</td>\n' +
            '                        <td onclick="loadDraft(&quot;' + recipient + '&quot;,&quot;' + subject + '&quot;,&quot;' + content + '&quot;)">' + content + '</td>\n' +
            '                    </tr>');
    }
}
function loadDraft(recipient, subject, content) {
    window.location = "writeMail.jsp?recipient="+recipient+"&subject="+subject+"&content="+content+"";
    var re = GetRequest().recipient;
    var su = GetRequest().subject;
    var co = GetRequest().content
    $("#recipient").html(re);
    $("#subject").html(su);
    $("#content").html(co);
}
//操作草稿箱
function UpdateDraft() {
    if(op1==1){
        id = "";
    }
    $.ajax({
        type:"post",
        url:"UpdateDraftBox",
        data:{
            op1:op1,
            recipient:$("#recipient").val(),
            subject:$("#subject").val(),
            content:$("#content").val(),
        },
        async:false,
        success:function (data) {
            if(data=="1") {
                toastr.success('操作成功');
            }
            else
                toastr.error('操作失败');
        }
    });
}
function fun1(){
    var obj = document.getElementsByName("DraftsSelect");
    var checkedNum = new Array() ;
    var id = new Array();
    var num = 0;
    for(var i=0 ;i<obj.length;i++){
        if(obj[i].checked){
            checkedNum[num] = obj[i].value;
            id[num] = allDrafts[checkedNum[num]].id;
            num++;
        }
    }
    return id;
}

// a.addEventListener("paste", function (e){
//     if ( !(e.clipboardData && e.clipboardData.items) ) {
//         return;
//     }
// });
// function dealImg(a,b) {
//     var c = new FormData;
//     c.append("text",$("#content").val());
//     c.append("upload_file",b);
//     console.log("c:"+c);
//     var d;
//     d = $.ajaxSettings.xhr();
//     d.withCredentials = i;
//     var f = $.ajax({
//         type:"post",
//         url:"sendMail",
//         data:c,
//         xhr:function () {
//             return d;
//         }
//     }).done(function (a) {
//     })
//     a.clipboardData
//
// }

// xhr.timeout = 3000;
// xhr.ontimeout = function(event){
//     alert('请求超时！');
// }
// var formData = new FormData();
// formData.append('username', '张三');
//
// formData.append('id', 123456);
// xhr.send(formData);
//
// var form = document.getElementById('myform');
//
// var formData = new FormData(form);
//
// formData.append('secret', '123456'); // 添加一个表单项
//
//
// var formData = new FormData();
//
// for (var i = 0; i < files.length;i++) {
//
//     formData.append('files[]', files[i]);
//
// }
// xhr.open('POST', form.action);
// xhr.send(formData);