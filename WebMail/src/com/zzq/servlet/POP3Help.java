package com.zzq.servlet;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

public class POP3Help {
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";//ssl加密
    public static Folder getFolder(String host, String username, String password) {
        Properties prop = new Properties();
        //选择指定的协议
        // "mail.host"指定发送、接受邮件的默认服务器；
        // "mail.store.protocol",指定接收的协议;
        // "mail.transport.protocal",指定发送的协议
        prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.pop3.socketFactory.fallback", "false");
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", host);
        prop.put("mail.pop3.auth.plain.disable","true");
        prop.put("mail.pop3.ssl.enable", "true");
        //创建邮件会话，Authenticator ：认证者，用不着
        Session mailSession = Session.getDefaultInstance(prop, null);
        mailSession.setDebug(false);

        try {
            //获取Store对象
            Store store = mailSession.getStore("pop3");
            //POP3服务器的登陆认证
            store.connect(host, username, password);
            //获取Folder对象，通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
            Folder folder = store.getFolder("INBOX");
            //指定邮件文件夹访问权限
            folder.open(Folder.READ_WRITE);
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}