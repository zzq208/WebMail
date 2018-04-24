package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.valuebean.Mail;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.zzq.toolsBean.StringUtil.changTime;

public class createConnect {
    public static void main(String[] args){
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        Properties prop = new Properties();
        //选择指定的协议
        // "mail.host"指定发送、接受邮件的默认服务器；
        // "mail.store.protocol",指定接收的协议;
        // "mail.transport.protocal",指定发送的协议
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", "pop.163.com");
        //创建邮件会话，Authenticator ：认证者，用不着
        Session mailSession = Session.getDefaultInstance(prop, null);
        mailSession.setDebug(false);

        try {
            //获取Store对象
            Store store = mailSession.getStore("pop3");
            //POP3服务器的登陆认证
            store.connect("pop.163.com", "17816869570@163.com","Zzq123456" );
            //获取Folder对象，通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
            Folder folder = store.getFolder("INBOX");
            //指定邮件文件夹访问权限
            folder.open(Folder.READ_WRITE);
            //邮件列表
            List<Mail> mailList = new ArrayList<Mail>();
            //转化为json
            ObjectMapper mapper = new ObjectMapper();
            Message[] messages = folder.getMessages();
            endTime = System.currentTimeMillis();
            System.out.println("程序1运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间;
            int count = folder.getMessageCount();
            startTime = System.currentTimeMillis();
            for (int i=0;i<10;i++) {
                Message msg = messages[i];
            //for(Message msg : messages){
                //startTime = System.currentTimeMillis();
                String from = (msg.getFrom()[0]).toString().replace("\"", "");
                //解决发件人乱码问题，乱码原因是由于在SMTP传输时不能使用中文字符，所以通过MimeUtility方法的encodeText编码，存在两种手段编码，一种是Base64，一种是QP。
                from = MimeUtility.decodeText(from);
                //获得主题
                String subject = msg.getSubject();
                //获得发送日期
                String sendDate = changTime(msg.getSentDate());
                Mail mail = new Mail();
                mail.setFrom(from);
                mail.setSubject(subject);
                mail.setSendDate(sendDate);
                mailList.add(mail);
            }
            endTime = System.currentTimeMillis();
            System.out.println("程序2运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}