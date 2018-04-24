package com.zzq.servlet;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.*;

public class IMAPHelp {
    public static Session getSession(String host, String username, String password) throws GeneralSecurityException {
        Properties prop = new Properties();
        //MailSSLSocketFactory sf = new MailSSLSocketFactory();
        //选择指定的协议
        // "mail.host"指定发送、接受邮件的默认服务器；
        // "mail.transport.protocal",指定发送的协议
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", host);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        //qq邮箱发送需要SSl连接协议
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        //props.put("mail.smtp.ssl.enable", "true");

        Authenticator authenticator = new Authenticator(){


            @Override

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username,password);

            }


        };

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props,authenticator);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        return session;
    }
}