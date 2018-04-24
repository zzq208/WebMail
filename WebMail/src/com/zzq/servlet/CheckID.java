package com.zzq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zzq.dao.DB;
import com.zzq.valuebean.UserBean;

public class CheckID extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        DB db = new DB();

        UserBean user = new UserBean();
        //获取账号密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //注入user中
        user.setUsername(username);
        user.setPassword(password);
        //验证user是否存在
        boolean flag = db.userLogin(user);
        //向Ajax输出
        PrintWriter out = response.getWriter();
        if(flag){
            HttpSession session = request.getSession();
            String user_id = null;
            //从数据库中取出该用户的id
            ResultSet rs = null;
            try {
                //查找相同账号用户的结果集
                rs = db.executeQuery("SELECT * FROM `user` WHERE `username` = '"+username+"'");
                //取一个
                rs.first();
                user_id = rs.getString(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setUser_id(user_id);
            session.setAttribute("loginer",user);//将当前用户注入到session会话的loginer属性中
            out.write("right");
            out.close();
        }
        else {
            out.write("false");
            out.close();
        }

    }
}
