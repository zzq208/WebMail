package web.垃圾箱;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class DisplayContent extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream sos = response.getOutputStream();
        HttpSession session = request.getSession();
        int msgnum = Integer.parseInt(request.getParameter("msgnum"));
        Folder folder = (Folder) session.getAttribute("folder");

        try {
            Message msg = folder.getMessage(msgnum);
            // 邮件类型不是mixed时，表示邮件中不包含附件，直接输出邮件内容
            if (!msg.isMimeType("multipart/mixed")) {
                //response.setHeader("Content-type","text/html;charset=UTF-8");
                response.setContentType("message/rfc822");
                msg.writeTo(sos);
            } else {
                // 查找并输出邮件中的邮件正文
                Multipart mp = (Multipart) msg.getContent();
                int bodynum = mp.getCount();
                for (int i = 0; i < bodynum; i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    /*
                     * MIME消息头中不包含disposition字段， 并且MIME消息类型不为mixed时，
                     * 表示当前获得的MIME消息为邮件正文
                     */
                    if (!bp.isMimeType("multipart/mixed") && bp.getDisposition() == null) {
                        //response.setHeader("Content-type","text/html;charset=UTF-8");
                        response.setContentType("message/rfc822");
                        bp.writeTo(sos);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}