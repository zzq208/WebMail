package com.zzq.servlet;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.runtime.JSONListAdapter;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static javax.mail.internet.MimeUtility.decodeText;

public class GetMailContent extends HttpServlet{
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
        response.setCharacterEncoding("UTF-8");
        int num = Integer.parseInt(request.getParameter("num"));
        int page = Integer.parseInt(request.getParameter("page"));
        int once = Integer.parseInt(request.getParameter("once"));
        StringBuffer content = new StringBuffer();
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Folder folder = (Folder) session.getAttribute("folder");
        try{
            int count = folder.getMessageCount();
            int msgnum = count - num;
            //System.out.println(msgnum);
            //得到邮件
            Message msg = folder.getMessage(msgnum);
            //解析邮件
            getMailContent(msg,content);
            out.write(content.toString());
            //附件编号
            int attachmentNum=0;
            List<Map> list = new ArrayList<>();
            saveAttachment(msg,attachmentNum,list);
            ObjectMapper mapper = new ObjectMapper();
            //得到附件信息
            String a = mapper.writeValueAsString(list);
            session.setAttribute("attachment",a);
            System.out.println(a);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取邮件体各部分, StringBuffer content用来存放邮件文本内容的字符串
    public static void getMailContent(Part part, StringBuffer content) throws Exception {
        String contentType = part.getContentType();
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        //System.out.println("CONTENTTYPE: " + contentType);
        //如果纯文本或部分html，不需要处理
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append((String) part.getContent());
        }
        //如果是混合节点部分，需要递归解析
        else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i),content);
            }
        }
        //如果是嵌套的邮件，递归解析
        else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent(),content);
        } else {}
    }
    // 保存附件
    public static void saveAttachment(Part part,int attachmentNum,List<Map> list) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    String fileName = decodeText(bodyPart.getFileName());
                    attachmentNum += 1;
                    Map<String,Object> map = new HashMap<>();
                    map.put("fileName", fileName);
                    map.put("attachmentNum", attachmentNum);
                    list.add(map);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart,attachmentNum, list);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        String fileName = decodeText(bodyPart.getFileName());
                        attachmentNum += 1;
                        Map<String,Object> map = new HashMap<>();
                        map.put("fileName", fileName);
                        map.put("attachmentNum", attachmentNum);
                        list.add(map);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(),attachmentNum,list);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     * @param is 输入流
     * @param fileName 文件名
     * @param destDir 文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

}
