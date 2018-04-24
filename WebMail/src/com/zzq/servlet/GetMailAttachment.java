package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.valuebean.Mail;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;

public class GetMailAttachment extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String a =(String)session.getAttribute("attachment");
        //如果a为空或者内容为空
        if(a==null||a.length()==2){
            out.write("noneAttachment");
            out.close();
        }
        else {
            System.out.println(a);
            session.removeAttribute("attachment");
            out.write(a);
            out.close();
        }
    }

}
