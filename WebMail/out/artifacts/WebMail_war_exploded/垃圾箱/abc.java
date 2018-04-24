package web.垃圾箱;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class abc {
    // static int port = 587;

    static String server = "smtp.exmail.qq.com";// 邮件服务器mail.cpip.net.cn

    static String from = "AA";// 发送者,显示的发件人名字

    static String user = "aa@aa.com";// 发送者邮箱地址

    static String password = "aaaa";// 密码

    public static void sendEmail() throws UnsupportedEncodingException {
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Properties props = new Properties();
            props.setProperty("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.store.protocol", "smtp");
            props.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });
            MimeMessage msg = new MimeMessage(session);
            String nick=javax.mail.internet.MimeUtility.encodeText("aaa");
            msg.setFrom(new InternetAddress(user, nick));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("11@qq.com", false));
            msg.setSubject("testest");
            msg.setSentDate(new Date());

            MimeMultipart multipart = new MimeMultipart("mixed");
            // 邮件内容，采用HTML格式
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.removeHeader("Content-Type");
            messageBodyPart.removeHeader("Content-Transfer-Encoding");
            messageBodyPart.addHeader("Content-Type", "text/html; charset=gbk");
            messageBodyPart.addHeader("Content-Transfer-Encoding", "base64");
            messageBodyPart.setContent(getHtml(), "text/html;charset=GBK");


            multipart.addBodyPart(messageBodyPart);
            //内嵌图片
            messageBodyPart=new MimeBodyPart();
            //DataSource dataSource=new FileDataSource("d:/1.png");
            URL url;
            try {
                url = new URL("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
                DataSource dataSource=new URLDataSource(url);
                DataHandler dataHandler=new DataHandler(dataSource);
                messageBodyPart.setDataHandler(dataHandler);
                messageBodyPart.setContentID("testi");

                multipart.addBodyPart(messageBodyPart);

                //添加附件
//                messageBodyPart=new MimeBodyPart();
//                DataSource dataSource1=new FileDataSource("d:/aa.doc");
//                dataHandler=new DataHandler(dataSource1);
//                messageBodyPart.setDataHandler(dataHandler);
//                messageBodyPart.setFileName(MimeUtility.encodeText(dataSource1.getName()));

                messageBodyPart=new MimeBodyPart();
//                InputStream is=downloadFtp();
//                //DataSource dataSource1=new FileDataSource("d:/aa.doc");
//                DataSource dataSource1=new ByteArrayDataSource(is, "application/png");
//                dataHandler=new DataHandler(dataSource1);
//                messageBodyPart.setDataHandler(dataHandler);
//                messageBodyPart.setFileName(MimeUtility.encodeText("aa.doc"));

//                multipart.addBodyPart(messageBodyPart);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            msg.setContent(multipart);
            msg.saveChanges();

            Transport.send(msg);

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String getHtml(){
        InputStream is;
        try {
            is = new FileInputStream("d:/测试.html");
            byte[] b = new byte[1024];
            int size = is.read(b);
            return new String(b,0,size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static void main(String args[]) throws UnsupportedEncodingException {

        sendEmail();// 收件人
        //System.out.println(getHtml());
        //downloadFtp();
        System.out.println("ok");
    }

//    public static InputStream downloadFtp(){
//        try {
//            FtpUtils ftpUtils = new FtpUtils();
//            FtpConfig config = new FtpConfig();
//            config.setEncode("GBK");// 设置编码
//            config.setServer("101.31.116.513");// 设置服务
//            config.setPort(21);// 设置端口
//            config.setUsername("weblogic");// 设置用户名
//            config.setPassword("weblogic");// 设置密码
//            config.setLocation("/home/weblogic/ebiz/mailtest");
//            boolean flag = ftpUtils.connectServer(config);
//            System.out.println(flag);
//            System.out.println(ftpUtils.getFileList());
//            InputStream  is=ftpUtils.getFtpClient().retrieveFileStream("aa.doc");
//            ftpUtils.closeServer();
//            return is;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
}