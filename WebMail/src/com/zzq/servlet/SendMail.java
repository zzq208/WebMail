package com.zzq.servlet;

import com.zzq.dao.DB;
import com.zzq.valuebean.UserBean;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMail extends HttpServlet{
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException{
        String recipient = request.getParameter("recipient");
        String subject = request.getParameter("subject");
        String attachment = request.getParameter("attachment");
        String content = request.getParameter("content");
        String formData = request.getParameter("formData");
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("loginer");
        String SMTPServer;
        String nickName;
        String mailUsername;
        String mailPassword;
        ResultSet rs;
        DB db = new DB();
        rs = db.executeQuery("SELECT * FROM `mailsettinglist` WHERE `is_use` = 1 AND `user_id` = "+user.getUser_id()+"");
        try{
            rs.next();
            nickName = rs.getString(2);
            SMTPServer = rs.getString(3);
            mailUsername = rs.getString(5);
            mailPassword = rs.getString(6);
            //创建并获取邮件会话
            Session mailSession = IMAPHelp.getSession(SMTPServer,mailUsername,mailPassword);
            //创建一封邮件
            MimeMessage msg = createMimeMessage(mailSession,mailUsername,recipient,nickName,subject,attachment,content);
            //根据 Session 获取邮件传输对象
            Transport transport = mailSession.getTransport();
            //使用邮箱账号和密码连接邮件服务器
            //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
            transport.connect(mailUsername, mailPassword);
            //发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg,msg.getAllRecipients());
            //关闭连接
            transport.close();

            PrintWriter out = response.getWriter();
            out.write("1");
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 创建一封复杂邮件（文本+图片+附件）
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String nickname,String subject, String attach, String content) throws Exception {
        // 1. 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, nickname, "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail, "我", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");

        /*
         * 下面是邮件内容的创建:
         */

        // 5. 创建图片“节点”
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("C:\\Users\\zzq\\Pictures\\图片1.png")); // 读取本地文件
        image.setDataHandler(dh);                   // 将图片数据添加到“节点”
        image.setContentID("image_fairy_tail");     // 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）
        //content = content+"<img src=\\\"cid:image_fairy_tail\\\">";
        // 6. 创建文本“节点”
        MimeBodyPart text = new MimeBodyPart();
        //    这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent(content, "text/html;charset=UTF-8");

        // 7. （文本+图片）设置 文本 和 图片 “节点”的关系（将 文本 和 图片 “节点”合成一个混合“节点”）
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.addBodyPart(image);
        mm_text_image.setSubType("related");    // 关联关系

        // 8. 将 文本+图片 的混合“节点”封装成一个普通“节点”
        //    最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        //    上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);

        // 9. 创建附件“节点”
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler dh2 = new DataHandler(new FileDataSource("C:\\Users\\zzq\\Desktop\\Interfaces.docx"));  // 读取本地文件
        attachment.setDataHandler(dh2);                                             // 将附件数据添加到“节点”
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));              // 设置附件的文件名（需要编码）

        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text_image);
        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");         // 混合关系

        // 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
        message.setContent(mm);

        // 12. 设置发件时间
        message.setSentDate(new Date());

        // 13. 保存上面的所有设置
        message.saveChanges();

        return message;
    }
}
