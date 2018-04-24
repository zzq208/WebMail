package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.dao.DB;
import com.zzq.valuebean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateDraftBox extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op1 = request.getParameter("op1");
        String id = request.getParameter("id");
        String recipient = request.getParameter("recipient");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");
        String checkedNum = request.getParameter("checkedNum");
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("loginer");
        ObjectMapper mapper = new ObjectMapper();
        String[] checkedArray=null;
        if(checkedNum != null) {
            checkedArray = mapper.readValue(checkedNum, String[].class);
        }
        DB db = new DB();
        String user_id = user.getUser_id();
        boolean flag = false;
        if(op1.equals("1")){
            flag = db.executeUpdate("INSERT INTO `draft` (`id`, `recipient`, `subject`, `content`, `user_id`) VALUES (NULL, '"+recipient+"', '"+subject+"', '"+content+"', '"+user_id+"')");
        }
        if(op1.equals("0")){
            flag = db.executeUpdate("UPDATE `draft` SET `recipient` = '"+recipient+"', `subject` = '"+subject+"', `content` = '"+content+"' WHERE `draft`.`id` = "+id+"");
        }
        if(op1.equals("2")){
            flag = true;
            for(int i=0;i<checkedArray.length;i++) {
                flag = db.executeUpdate("DELETE FROM `draft` WHERE `draft`.`id` = " + checkedArray[i] + "");
            }
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