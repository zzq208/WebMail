package com.zzq.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.mail.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static javax.mail.internet.MimeUtility.decodeText;

public class HandleAttach extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();
        int num = Integer.parseInt(request.getParameter("num"));
        int attachmentNum = Integer.parseInt(request.getParameter("attachmentNum"));
        String fileName = request.getParameter("fileName");
        //解决中文文件名不显示问题，需要ISO-8859-1编码
        fileName = new String(fileName.getBytes(),"ISO-8859-1");
        Folder folder = (Folder) session.getAttribute("folder");
        try {
            int count = folder.getMessageCount();
            int msgnum = count - num + 1;
            Message msg = folder.getMessage(msgnum);
            System.out.println(msg.getSubject());
            // 将消息头类型设置为附件类型
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            int cnum = 0;
            HandleAttachment(msg,cnum,attachmentNum,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* 保存附件
    part:内容
    num:计数
    attachmentNum:附件编号
    out:输出文件
     */
    public static void HandleAttachment(Part part, int num,int attachmentNum,ServletOutputStream out) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    num += 1;
                    if(num == attachmentNum){
                        copy(is,out);
                    }
                } else if (bodyPart.isMimeType("multipart/*")) {
                    HandleAttachment(bodyPart, num, attachmentNum, out);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        num += 1;
                        if(num == attachmentNum){
                            copy(bodyPart.getInputStream(),out);
                        }
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            HandleAttachment((Part) part.getContent(), num, attachmentNum, out);
        }
    }
    //将文件生成InputStream给用户下载
    public static void copy(InputStream is, ServletOutputStream os) throws IOException {
        int c = 0;
        while ((c = is.read()) != -1) {
            os.write(c);
        }
    }
}