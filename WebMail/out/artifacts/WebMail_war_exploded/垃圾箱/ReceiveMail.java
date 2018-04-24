package web.垃圾箱;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

class a{
    public static void main(String[] args) {
        try {
            pop3();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void pop3() throws Exception {
        // 定义连接POP3服务器的属性信息
        String pop3Server = "pop.163.com";
        String protocol = "pop3";
        String username = "17816869570@163.com";
        String password = "Zzq123456"; //此处是用户自己设置的授权码

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol); // 使用的协议
        props.setProperty("mail.smtp.host", pop3Server); // 发件人的SMTP服务器地址

        // 获取连接
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        // 获取Store对象
        Store store = session.getStore(protocol);
        store.connect(pop3Server, username, password); // POP3服务器的登陆认证

        //通过 POP3协议获得Store对象，调用getFolder()，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限

        Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件

        for (Message message : messages) {
            String subject = message.getSubject();// 获得邮件主题
            Address from = (Address) message.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
            System.out.println("邮件的内容为：");
            message.writeTo(System.out);// 输出邮件内容到控制台
        }

        folder.close(false);// 关闭邮件夹对象
        store.close(); // 关闭连接对象
    }
}
public class ReceiveMail extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //创建操作json的ObjectMapper对象
        ObjectMapper mapper = new ObjectMapper();
    }
}