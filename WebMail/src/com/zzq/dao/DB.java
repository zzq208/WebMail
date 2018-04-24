package com.zzq.dao;

import com.zzq.valuebean.UserBean;
import java.sql.*;


public class DB {
    private String url = "jdbc:mysql://localhost:3306/mail?characterEncoding=utf8";
    private String className = "com.mysql.jdbc.Driver";
    private String userName = "root";
    private String password = "";
    private Connection con = null;
    private Statement stm=null;
    //通过构造方法加载数据库驱动
    public DB(){
        try {
            Class.forName(className).newInstance();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("加载数据库驱动失败");
        }
    }
    //创建数据库连接
    public void createCon(){
        try {
            con = DriverManager.getConnection(url, userName, password);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("获取数据库连接失败");
        }
    }
    //获取Statement对象
    public void getStm(){
        createCon();
        try {
            stm = con.createStatement();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("创建Statement对象失败");
        }
    }
    //对数据库的增加、修改、删除操作，参数为要执行的SQL,返回boolean值
    public boolean executeUpdate(String sql) {
        boolean mark = false;
        try {
            getStm();
            int iCount = stm.executeUpdate(sql);
            if (iCount > 0)
                mark = true;
            else
                mark = false;
        } catch (Exception e) {
            e.printStackTrace();
            mark = false;
        }
        return mark;
    }
    //查询数据库
    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            getStm();
            try {
                rs = stm.executeQuery(sql);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("查询数据库失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    //判断用户登录结果
    public boolean userLogin(UserBean user) {
        boolean flag = false;
        ResultSet rs = null;
        try{
            rs = executeQuery("SELECT * FROM user");
            rs.beforeFirst();
            while(rs.next()){
                if(rs.getString(2).equals(user.getUsername())&&rs.getString(3).equals(user.getPassword())){
                    flag = true;
                    rs.getString(1);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
