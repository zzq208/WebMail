package com.zzq.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzq.dao.DB;
import com.zzq.valuebean.Drafts;
import com.zzq.valuebean.MailAccount;
import com.zzq.valuebean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GetDrafts extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserBean user = (UserBean)session.getAttribute("loginer");
        String user_id = user.getUser_id();
        ResultSet rs = null;
        List<Drafts> draftsLsit = new ArrayList<Drafts>();
        DB db = new DB();
        rs = db.executeQuery("SELECT * FROM `draft` WHERE `user_id`="+user_id+"");
        try{
            rs.beforeFirst();
            while(rs.next()){
                Drafts drafts = new Drafts();
                drafts.setId(rs.getString(1));
                drafts.setRecipients(rs.getString(2));
                drafts.setSubject(rs.getString(3));
                drafts.setContent(rs.getString(4));
                draftsLsit.add(drafts);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        //将drafts转换成json
        String jsonlist = mapper.writeValueAsString(draftsLsit);
        PrintWriter out = response.getWriter();
        out.write(jsonlist);
        out.close();
    }
}