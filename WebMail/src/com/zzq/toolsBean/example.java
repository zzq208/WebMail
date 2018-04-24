package com.zzq.toolsBean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class example {
    public static void main(String[] args) throws MessagingException{
        String sub = "JavaMail测试";
        String tex = "这是一封由JavaMail发送的邮件！";
        String receiver = "294264338@qq.com";

        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject(sub);
        // 设置邮件内容
        msg.setText(tex);
        // 设置发件人
        msg.setFrom(new InternetAddress("17816869570@163.com"));

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect("17816869570", "Zzq123456");
        // 发送邮件
        transport.sendMessage(msg, new Address[] {new InternetAddress(receiver)});
        transport.close();
    }
}
