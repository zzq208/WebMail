package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.valuebean.Mail;

import javax.mail.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static com.zzq.toolsBean.StringUtil.*;


public class GetMailList extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        response.setCharacterEncoding("UTF-8");
        String host = request.getParameter("host");
        String mailUsername = request.getParameter("maiUsername");
        String mailPassword = request.getParameter("maiPassword");
        String page = request.getParameter("page");
        int pg = Integer.parseInt(page);
        Folder folder = POP3Help.getFolder(host, mailUsername, mailPassword);
        HttpSession session = request.getSession();
        session.setAttribute("host",host);
        session.setAttribute("maiUsername",mailUsername);
        session.setAttribute("maiPassword",mailPassword);
        session.setAttribute("folder", folder);
        //邮件列表
        List<Mail> mailList = new ArrayList<Mail>();
        //转化为json
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message[] messages = folder.getMessages();
            //邮件的数量
            int count = folder.getMessageCount();
            //一页显示多少封邮件
            int once = 25;
            request.setAttribute("once", once);
            int lastPage = 0;
            if (count % once == 0) {
                lastPage = count / once;
            } else {
                lastPage = count / once + 1;
            }
            int firstNum = count - ((pg - 1) * once) - 1;//显示的第一封编号
            int lastNum = count - (pg * once);//显示的最后一封编号
            for (int i = firstNum; lastNum <= i; i--) {
                if (i >= 0) {
                    Message msg = messages[i];
                    String from = (msg.getFrom()[0]).toString().replace("\"", "");
                    //System.out.println(from);
                    //解决发件人乱码问题，乱码原因是由于在SMTP传输时不能使用中文字符，所以通过MimeUtility方法的encodeText编码，存在两种手段编码，一种是Base64，一种是QP。
                    from = MimeUtility.decodeText(from);
                    //获得主题
                    String subject = msg.getSubject();
                    if (subject == null || subject.length() <= 0) {
                        subject = "(无主题)";
                    }
                    //获得发送日期
                    String sendDate = changTime(msg.getSentDate());
                    String recipients = getReceiveAddress(msg, null);
                    //判断是否存在附件
                    String isAttachment;
                    boolean isContainAttachment = isContainAttachment((Part) msg);
                    if (isContainAttachment) {
                        isAttachment = "1";
                    } else isAttachment = "0";
                    Mail mail = new Mail();
                    mail.setFrom(from);
                    mail.setSubject(subject);
                    mail.setSendDate(sendDate);
                    mail.setRecipients(recipients);
                    mail.setIsAttachment(isAttachment);
                    mailList.add(mail);
                } else
                    break;
            }
            //将mailAccount转换成json
            String jsonlist = mapper.writeValueAsString(mailList);
            PrintWriter out = response.getWriter();
            //将页数添加到json里
            StringBuffer stringBuffer = new StringBuffer(jsonlist);
            stringBuffer.insert(1, "{\"page\": " + lastPage + ",\"count\": " + count + "},");
            //System.out.println(stringBuffer);
            out.write(stringBuffer.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //得到全部收件人
    public static String getReceiveAddress(Message msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString().replace("\"", "")).append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length() - 1); //删除最后一个逗号

        return receiveAddress.toString();
    }

    //判断邮件中是否包含附件
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                //该邮件可能是由复杂邮件套复杂邮件组成，因此要递归得到该邮件最终是否包含附件
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }
}
