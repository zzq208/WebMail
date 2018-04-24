package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class DeleteMail extends HttpServlet{
        public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String checkedNum = request.getParameter("checkedNum");
            HttpSession session = request.getSession();
            String host = (String) session.getAttribute("host");
            String mailUername = (String) session.getAttribute("maiUsername");
            String maiPassword = (String) session.getAttribute("maiPassword");
            Folder folder = POP3Help.getFolder(host , mailUername, maiPassword);
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String[] checkedArray = mapper.readValue(checkedNum, String[].class);
            try {
                int count = folder.getMessageCount();
                for(int i=0;i<checkedArray.length;i++){
                    int a = Integer.parseInt(checkedArray[i]);
                    System.out.println(a);
                    Message message = folder.getMessage(count-a+1);
                    String s = message.getSubject();
                    message.setFlag(Flags.Flag.DELETED, true);//设置已删除状态为true
                }
                folder.close(true);
                out.write("1");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
}
