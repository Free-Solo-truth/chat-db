package com.example.foodrecipes.model;

import java.io.Serializable;

import kotlin.reflect.jvm.internal.impl.load.java.Constant;

/*实现对象输入输出流*/
public class MsgData implements Serializable{
      static int SEND_TYPE = 1;
       static int REVICE_TYPE = 0;
    private String sendName;
    private String receiveName;
    private String content;
    private int type;
    private int what;
    
    public MsgData(String sendName, String receiveName, String content,int type,int what) {
        this.sendName = sendName;
        this.receiveName = receiveName;
        this.content = content;
        this.type = type;
        this.what = what;
    }
    public static int getSendType(){return SEND_TYPE;}
    public static int getReviceType(){return REVICE_TYPE;}

    public int getType(){return type;}

    public void setType(int type){ this.type = type;}

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }
    
    
    public String getSendName() {
        return sendName;
    }

    public void setSendID(String sendID) {
        this.sendName = sendID;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
