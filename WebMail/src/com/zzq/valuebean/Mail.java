package com.zzq.valuebean;

public class Mail {
    private String from;
    private String subject;
    private String sendDate;
    private String recipients;
    private String receivedDate;
    private String ContentType;
    private String isAttachment;

    public Mail(){}

    public void setFrom(String from){
        this.from = from;
    }
    public String getFrom(){
        return from;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }

    public void setSendDate(String sendDate){
        this.sendDate = sendDate;
    }
    public String getSendDate(){
        return sendDate;
    }

    public void setReceivedDate(String receivedDate){
        this.receivedDate = receivedDate;
    }
    public String getReceivedDate(){
        return receivedDate;
    }

    public void setContentType(String ContentType){
        this.ContentType = ContentType;
    }
    public String getContentType(){
        return ContentType;
    }

    public void setRecipients(String recipients){
        this.recipients = recipients;
    }
    public String getRecipients(){
        return recipients;
    }

    public void setIsAttachment(String isAttachment){
        this.isAttachment = isAttachment;
    }
    public String getIsAttachment(){
        return isAttachment;
    }


}
