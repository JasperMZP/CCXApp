package com.example.jasper.ccxapp.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jasper on 2017/3/21.
 */

public class User extends BmobObject {

    private String id;
    private String username;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
