package web.垃圾箱;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class DisplayHead extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=gb2312");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int msgnum = Integer.parseInt(request.getParameter("msgnum"));
        Folder folder = (Folder) session.getAttribute("folder");

        try {
            Message msg = folder.getMessage(msgnum);
            String from = msg.getFrom()[0].toString();
            String subject = msg.getSubject();
            String sendDate = DateFormat.getInstance().format(msg.getSentDate());

            out.println("邮件主题：" + subject + "<br/>");
            out.println("发件人:" + from + "<br/>");
            out.println("发送日期：" + sendDate + "<br/><br/>");

            System.out.println("contentType：" + msg.getContentType());

            // 如果该邮件是组合型"multipart/*"则可能包含附件等
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);

                    // 如果该BodyPart对象包含附件，则应该解析出来
                    if (bp.getDisposition() != null) {
                        String filename = bp.getFileName();
                        System.out.println("filename：" + filename);

                        if (filename.startsWith("=?")) {
                            // 把文件名编码成符合RFC822规范
                            filename = MimeUtility.decodeText(filename);
                        }

                        // 生成打开附件的超链接
                        out.print("附件：");
                        out.print("<a href=HandleAttach?msgnum=" + msgnum + "&&bodynum=" + i + "&&filename=" + filename
                                + ">" + filename + "</a><br/>");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}