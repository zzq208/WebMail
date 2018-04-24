package com.zzq.toolsBean;

import java.util.Date;
import java.text.SimpleDateFormat;

public class StringUtil {
    //将HTML特殊字符替换
    public static String changHTML(String value){
        value = value.replace("&","&amp;");
        value = value.replace(" ","&nbsp;");
        value = value.replace("<","&lt;");
        value = value.replace(">","&gt;");
        value = value.replace("\r\n","&<br>");
        return value;
    }
    //将时间转化成固定格式
    public static String changTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日(E) aHH:mm");
        return format.format(date);
    }

    public static String changTimeToSimple(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    //将字符串转化为int数字
    public static int strToint(String value){
        int i=-1;
        if(value==null||value.equals(""))
            return i;
        try{
            i = Integer.parseInt(value);
        }catch (NumberFormatException e){
            i = -1;
            e.printStackTrace();
        }
        return i;
    }
}