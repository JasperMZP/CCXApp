package com.example.jasper.ccxapp.entitiy;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class User extends BmobObject {
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
