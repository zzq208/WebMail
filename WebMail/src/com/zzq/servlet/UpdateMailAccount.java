package com.zzq.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zzq.dao.DB;
import com.zzq.valuebean.UserBean;

public class UpdateMailAccount extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取邮箱账户表单内各个值
        String op = request.getParameter("op");
        String id = request.getParameter("id");
        String nickname = request.getParameter("nickName");
        String SMTPServer = request.getParameter("SMTPServer");
        String POP3Server = request.getParameter("POP3Server");
        String mailUsername = request.getParameter("mailUsername");
        String mailPassword = request.getParameter("mailPassword");
        //获取session中的用户id
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("loginer");
        String user_id = user.getUser_id();
        boolean flag = false;
        //将邮箱账户插入对应用户id的邮箱账户表中
        DB db = new DB();
        //System.out.println("op"+op);
        //System.out.println("id"+id);
        //System.out.println("nickname"+nickname);
        if(op.equals("0")) {
            flag = db.executeUpdate("UPDATE `mailsettinglist` SET `nickName` = '"+nickname+"', `SMTPServer` = '"+SMTPServer+"', `POP3Server` = '"+POP3Server+"', `mailUsername` = '"+mailUsername+"', `mailPassword` = '"+mailPassword+"' WHERE `mailsettinglist`.`id` = "+id+"");
        }
        if(op.equals("1")){
            flag = db.executeUpdate("INSERT INTO `mailsettinglist` (`id`, `nickName`, `SMTPServer`, `POP3Server`, `mailUsername`, `mailPassword`, `is_use`, `user_id`) VALUES (NULL, '" + nickname + "', '" + SMTPServer + "', '" + POP3Server + "', '" + mailUsername + "', '" + mailPassword + "', '0', '" + user_id + "')");
        }
        if(op.equals("2")){
            flag = db.executeUpdate("DELETE FROM `mailsettinglist` WHERE `mailsettinglist`.`id` = "+id+"");
        }
        if(op.equals("3")){
            flag = db.executeUpdate("UPDATE `mailsettinglist` SET `is_use` = '1' WHERE `mailsettinglist`.`id` = "+id+"")&&db.executeUpdate("UPDATE `mailsettinglist` SET `is_use` = '0' WHERE (`mailsettinglist`.`id` != "+id+" AND `mailsettinglist`.`user_id` = "+user_id+")");
        }
        PrintWriter out = response.getWriter();
        if(flag){
            out.write("1");
            out.close();
        }
        else{
            out.write("0");
            out.close();
        }

    }
}
