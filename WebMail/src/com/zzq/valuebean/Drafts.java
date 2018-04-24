package com.zzq.valuebean;

public class Drafts {
    private String id;
    private String subject;
    private String recipients;
    private String content;

    public Drafts(){}


    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return subject;
    }

    public void setRecipients(String recipients){
        this.recipients = recipients;
    }
    public String getRecipients(){
        return recipients;
    }

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return content;
    }

}
