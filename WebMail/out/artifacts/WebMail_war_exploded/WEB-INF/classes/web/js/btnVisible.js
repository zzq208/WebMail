$(document).ready(function() {
    $("div#nickNameList a").on('click', function() {
        $('#delete').show();
    });
    $("#addNickName").on('click', function() {
        $('#delete').hide();
    });
    //退出操作
    $("#signOut").on('click',function () {
        $.post("LoginServlet",{action:"logout"},function (data) {
            console.log(data);
            window.location="login.jsp";
        })
    });
});

/*
var http_request = false;
function createRequest(url) {
   http_request = false;
   if (window.XMLHttpRequest){
       http_request=new XMLHttpRequest();
       if(http_request.overrideMimeType){
           http_request.overrideMimeType('text/xml');//设置MIME类别
       }
   }else if(window.ActiveXObject){
       try{
           http_request = new ActiveXObject("Msxm12.XMLHTTP");
       }catch (e){
           try{
               http_request = new ActiveXObject("Microsoft.XMLHTTP");
           }catch (e){}
       }
   }
   if(!http_request){
       alert("不能创建XMLHttpRequest对象实例！");
       return false;
   }
   http_request.onreadystatechange = getResult;
   http_request.open('POST', url, true);
   http_request.send();
}
function getResult() {
    if(http_request.readyState == 4){ //判断请求状态
        if(http_request.status == 200){ //请求成功，开始处理返回结果
            $("#ajax").append("lalala");
            alert(http_request.responseText);
        }else{//请求页面有错误
            alert("您请求的页面有错误！");
        }
    }
}
createRequest('Ajax');
*/
function validateLogin(username,password) {
    var f = document.loginForm;
    var name=f.username;
    var psd=f.password;
    var mark=true;
    var message="";
    if(name==null||name.equal("")){
        mark=false;
        message+="请输入用户名！";
    }
    if(psd==null||psd.equal("")){
        mark=false;
        message+="请输入密码！";
    }
    return message;
}

function checkID() {
    $.ajax({
        type:"post",
        url:"CheckID",
        data:{
            username:$("#username").val(),
            password:$("#password").val()
        },
        success:function (data,textStatus) {
            if(data=="right"){
                alert("登陆成功");
                window.location="index.jsp";
            }
            else
                alert("用户名密码错误");
        }
    })
}

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
        success:function (data,textStatus) {
            if(data=="1"){
                alert("保存成功");
                $("#changeMail").modal('hide');
            }
            else
                alert("保存失败");
        }
    })
}
/*
public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    boolean flag=validateLogin(request,response);
    RequestDispatcher rd = null;
    if(flag){
        DB db = new DB();
        UserBean loginer = new UserBean();
        loginer.setUsername(request.getParameter("username"));
        loginer.setPassword(request.getParameter("password"));
        boolean mark = db.userLogin(loginer);
        if(!mark){//不存在用户
            request.setAttribute("messages","输入的用户名或密码错误!");
            rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
        else{//存在用户
            HttpSession session = request.getSession();
            //将当前登陆的用户注册到session中的loginer属性中
            session.setAttribute("loginer",loginer);
            response.sendRedirect("index.jsp");
        }
    }
    else {
        rd = request.getRequestDispatcher("login.jsp");
        rd.forward(request,response);
    }
}*/