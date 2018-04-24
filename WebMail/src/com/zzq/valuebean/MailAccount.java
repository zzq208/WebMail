package com.zzq.valuebean;

public class MailAccount {
    private String id;
    private String nickname;
    private String SMTPServer;
    private String POP3Server;
    private String mailUsername;
    private String mailPassword;
    private String is_use;

    public MailAccount(){}

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSMTPServer() {
        return SMTPServer;
    }
    public void setSMTPServer(String SMTPServer) {
        this.SMTPServer = SMTPServer;
    }

    public String getPOP3Server() {
        return POP3Server;
    }
    public void setPOP3Server(String POP3Server) {
        this.POP3Server = POP3Server;
    }

    public String getMailUsername() {
        return mailUsername;
    }
    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }
    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getIs_use() {
        return is_use;
    }
    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

}
