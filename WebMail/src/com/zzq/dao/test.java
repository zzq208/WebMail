package com.zzq.dao;

import javax.mail.internet.MimeUtility;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        DB test =new DB();
        test.createCon();
        String a = MimeUtility.decodeText("=?gbk?b?zfjS19HP0aE=?= <yanxuan1@service.netease.com>");
        System.out.println(a);
    }
}
