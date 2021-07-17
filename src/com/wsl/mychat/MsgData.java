package com.wsl.mychat;

import java.io.Serializable;

public class MsgData implements Serializable{
    private String sendID;
    private String receiveID;
    private String content;
    private int what;
    
    public MsgData(String sendID, String receiveID, String content) {
        this.sendID = sendID;
        this.receiveID = receiveID;
        this.content = content;
    }
    
    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }
    
    
    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
