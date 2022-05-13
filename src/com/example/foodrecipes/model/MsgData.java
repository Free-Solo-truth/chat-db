package com.example.foodrecipes.model;

import java.io.Serializable;


/*实现对象输入输出流*/

public class MsgData implements Serializable{
    private static final long serialVersionUID = 6975406383873487295L;
    private String sendName;
    private String receiveName;
    private String content;
    private int what;
    private int type;
    
    public MsgData(String sendName, String receiveName, String content,int type,int what) {
        this.sendName = sendName;
        this.receiveName = receiveName;
        this.content = content;
        this.type = type;
        this.what = what;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }
    
    
    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendID) {
        this.sendName = sendID;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveID(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
