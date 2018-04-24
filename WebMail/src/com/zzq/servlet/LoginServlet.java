package com.zzq.servlet;

// 导入必需的 java 库
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.zzq.dao.DB;
import com.zzq.valuebean.UserBean;

// 扩展 HttpServlet 类
public class LoginServlet extends HttpServlet {

    public void init() throws ServletException
    {
        // 执行必需的初始化
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        String action = request.getParameter("action");
        if(action==null)
            action="";
//        if(action.equals("islogin")){
//            this.isLogin(request,response);
//        }
//        if(action.equals("login")){
//            this.login(request,response);
//        }
        if(action.equals("logout")){
            this.logout(request,response);
        }
    }
//    //判断是否登陆
//    public void isLogin(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        String forward="";
//        HttpSession session=request.getSession();
//        if(session.getAttribute("loginer")!=null)
//            forward="index.jsp";
//        else
//            forward="login.jsp";
//        response.sendRedirect(forward);
//    }
//    //验证用户输入的表单数据是否为空
//    public boolean validateLogin(HttpServletRequest request,HttpServletResponse response){
//        boolean mark=true;
//        String messages="";//保存提示信息
//        String name=request.getParameter("username");
//        String password=request.getParameter("password");
//        if(name==null||name.equals("")){//username字段的验证
//            mark=false;
//            messages+="请输入用户名!";
//        }
//        if(password==null||password.equals("")){//password字段的验证
//            mark=false;
//            messages+="请输入密码!";
//        }
//        request.setAttribute("messages",messages);
//        return mark;//保存提示信息
//    }
//
//    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        boolean flag=validateLogin(request,response);
//        RequestDispatcher rd = null;
//        if(flag){
//            DB db = new DB();
//            UserBean loginer = new UserBean();
//            loginer.setUsername(request.getParameter("username"));
//            loginer.setPassword(request.getParameter("password"));
//            boolean mark = db.userLogin(loginer);
//            if(!mark){//不存在用户
//                request.setAttribute("messages","输入的用户名或密码错误!");
//                rd = request.getRequestDispatcher("login.jsp");
//                rd.forward(request, response);
//            }
//            else{//存在用户
//                HttpSession session = request.getSession();
//                //将当前登陆的用户注册到session中的loginer属性中
//                session.setAttribute("loginer",loginer);
//                response.sendRedirect("index.jsp");
//            }
//        }
//        else {
//            rd = request.getRequestDispatcher("login.jsp");
//            rd.forward(request,response);
//        }
//    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        session.removeAttribute("loginer");
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
        rd.forward(request, response);
    }
    public void destroy()
    {
        // 什么也不做
    }
}
