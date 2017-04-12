package com.example.jasper.ccxapp.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class Chat extends BmobObject {
    private String userName;
    private String chatroomName;
    private String otherUser;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }
}
