package com.zzq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zzq.dao.DB;
import com.zzq.valuebean.MailAccount;
import com.zzq.valuebean.UserBean;

public class GetMailAccount extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setCharacterEncoding("UTF-8");
        ResultSet rs = null;
        String user_id = null;

        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("loginer");
        user_id = user.getUser_id();


        List<MailAccount> mailAccountslist = new ArrayList<MailAccount>();

        /**
         * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
         * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
         * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
         * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
         * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
         * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
         */
        ObjectMapper mapper = new ObjectMapper();

        DB db = new DB();
        //从数据库中取出该用户所有的邮箱账户
        rs = db.executeQuery("SELECT * FROM `mailsettinglist` WHERE `user_id` = "+user_id+" ORDER BY `id` ASC");
        try {
            rs.beforeFirst();
            while(rs.next()){
                MailAccount mailAccount = new MailAccount();
                mailAccount.setId(rs.getString(1));
                mailAccount.setNickname(rs.getString(2));
                mailAccount.setSMTPServer(rs.getString(3));
                mailAccount.setPOP3Server(rs.getString(4));
                mailAccount.setMailUsername(rs.getString(5));
                mailAccount.setMailPassword(rs.getString(6));
                mailAccount.setIs_use(rs.getString(7));
                mailAccountslist.add(mailAccount);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //将mailAccount转换成json
        String jsonlist = mapper.writeValueAsString(mailAccountslist);
        PrintWriter out = response.getWriter();
        out.write(jsonlist);
        out.close();
    }
}
