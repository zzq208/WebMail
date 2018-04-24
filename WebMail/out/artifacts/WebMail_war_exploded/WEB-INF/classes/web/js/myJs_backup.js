//用户正在使用的邮箱账户，是一个json对象
var isUseMailAccount;
var mailList;//保存邮件列表json对象
var page=1;//页数
var lastpage=0;//最后一页页码
var count=0;//每页邮件个数
var allCount=0;//所有邮件个数

// if($.cookie("page",page)!=null){
//     page = parseInt($.cookie("page",page));
// }
// else {
//     $.cookie("page",page);
// }
// console.log($.cookie("page"));
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

$(document).ready(function() {
    //监听跳页按钮
    $("#previous").attr('style','background: #f0f0f0!important;color: #ccc!important;cursor: default');
    $("#previous").attr('disable',true);
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
    //退出操作
    $("#signOut").on('click',function () {
        $.post("LoginServlet",{action:"logout"}, function () {
            window.location="login.jsp";
        })
    });
    getMailAccountLists();
    if(GetRequest().num==null) {
        getMailList();
    }
    else {
        loadMailHead();
    }
});

//获得邮件内容
function getContent() {
    $.ajax({
        type:"post",
        url:"DisplayContent",
        data:{
            msgnum:1
        },
        async:true,
        success:function (data) {
        }
    });
    console.log("abc");
}
function loadContent() {
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
            //如果成功执行，载入邮件列表，并且在showMailAccountLists中顺便得到了用户正在使用的邮箱账户
            loadMailAccountLists(data);
            //显示邮件列表
            //getMailList();
            deleteDisplay();
            //getContent();
        }
    });
}

//得到邮件列表
function getMailList() {
    $.ajax({
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
    })
}

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
        var subject = encodeURIComponent(dataObj[i].subject);
        var from = encodeURIComponent(dataObj[i].from);
        var sendDate = encodeURIComponent(dataObj[i].sendDate);
        var recipients = encodeURIComponent(dataObj[i].recipients);
        var isAttachment = dataObj[i].isAttachment;
        $("#tbody").append('<tr onclick="window.location=&quot;watchMail.jsp?subject='+subject+'&&from='+from+'&&sendDate='+sendDate+'&&recipients='+recipients+'&&isAttachment='+isAttachment+'&&num='+i+'&quot;"><td><input type="checkbox"></td>\n' +
            '                        <th scope="row">'+num+'</th>\n' +
            '                        <td>'+dataObj[i].from+'</td>\n' +
            '                        <td>'+dataObj[i].subject+'</td>\n' +
            '                        <td>'+SimpleTime(dataObj[i].sendDate)+'</td>\n' +
            '                    </tr>');
        //$("#mainFrame").html();

    }
}
function loadMainFrom() {
    var mainFrame = $("#mainFrame").html();

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

function loadMailHead() {
    var num = GetRequest().num;
    var subject = GetRequest().subject;
    var from = GetRequest().from;
    var sendDate = GetRequest().sendDate;
    var recipients = GetRequest().recipients;
    var isAttachment = GetRequest().isAttachment;
    //recipients = "网易邮箱用户 <user@netease.com>,网易邮箱用户 <user@netease.com>,网易邮箱用户 <user@netease.com>"
    console.log(from);
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
    if(isAttachment == 1){
        $("#trAttachment").attr('style','display:block');
    }
}
//简化时间
function SimpleTime(Time) {
    Time = Time.substr(0,11);
    return Time;
}

//显示隐藏删除按钮
function deleteDisplay() {
    $("#addNickName").on('click', function() {
        $('#delete').hide();
    });
    $("div#nickNameList a").on('click', function() {
        $('#delete').show();
    });
}


//载入邮箱账户列表
function loadMailAccountLists(data) {
    var dataObj = JSON.parse(data);
    console.log(data);
    for (var i=0;i<dataObj.length;i++){
        $("#nickNameList").append("<a type=\"button\" class=\"list-group-item\" data-toggle=\"modal\" data-target=\"#changeMail\">"+dataObj[i].nickname+"&lt;"+dataObj[i].mailUsername+"&gt;</a>");
        if(dataObj[i].is_use==1){
            //把用户当前正在使用的账户记录下来
            isUseMailAccount=dataObj[i];
            console.log("use2 "+isUseMailAccount);
            $("#nick" ).empty();
            $("#mail" ).empty();
            $("#nick").append(isUseMailAccount.nickname);
            $("#mail").append(isUseMailAccount.mailUsername);
        }
    }
}

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
    $.ajax({
        type:"post",
        url:"SaveMailAccount",
        data:{
            nickName:$("#nickName").val(),
            SMTPServer:$("#SMTPServer").val(),
            POP3Server:$("#POP3Server").val(),
            mailUsername:$("#mailUsername").val(),
            mailPassword:$("#mailPassword").val()
        },
        success:function (data) {
            if(data=="1") {
                alert("保存成功");
                $("#changeMail").modal('hide');
                $("#nickNameList").empty();
                showMailAccountLists();
            }
            else
                alert("保存失败");
        }
    })
}

//设置弹出提示框的位置
toastr.options.positionClass = 'toast-center-center';
toastr.options.timeout = 10;

//反馈用户执行结果的提示框
function al() {
    $('#confirm').modal('hide');
    toastr.success('删除成功！');
}
function saveToDraftBox() {
    toastr.success('已保存到草稿箱');
}
//读取图片
var imgReader = function( item ){
    var file = item.getAsFile(),
        reader = new FileReader();

    // 读取文件后将其显示在网页中
    reader.onload = function( e ){
        var img = new Image();

        img.src = e.target.result;
        document.body.appendChild( img );
    };
    // 读取文件
    reader.readAsDataURL( file );
};

$("#content").on('paste', function(e) {
// 添加到事件对象中的访问系统剪贴板的接口
    var clipboardData = event.clipboardData,
        i = 0,
        items, item, types;

    if( clipboardData ){
        items = clipboardData.items;

        if( !items ){
            return;
        }

        item = items[0];
        // 保存在剪贴板中的数据类型
        types = clipboardData.types || [];

        for( ; i < types.length; i++ ){
            if( types[i] === 'Files' ){
                item = items[i];
                break;
            }
        }

        // 判断是否为图片数据
        if( item && item.kind === 'file' && item.type.match(/^image\//i) ){
            // 读取该图片
            imgReader( item );
        }
    }

    // for (var i = 0, len = e.clipboardData.items.length; i < len; i++) {
    //     var item = e.clipboardData.items[i];
    //     if (item.kind === "string") {
    //         item.getAsString(function (str) {
    //             alert(str);
    //         });
    //     } else if (item.kind === "file") {
    //         var pasteFile = item.getAsFile();
    //         // pasteFile就是获取到的文件
    //     }
    // }
});
// document.getElementById("content").addEventListener("paste", function (e){
//     console.log(e.clipboardData);
//     if ( !(e.clipboardData && e.clipboardData.items) ) {
//         return ;
//     }

// });

