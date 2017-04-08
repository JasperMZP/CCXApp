package com.example.jasper.ccxapp.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class Friend extends BmobObject {
    private String friend1;
    private String friend2;

    public Friend() {
    }


    public String getFriend1() {
        return friend1;
    }

    public void setFriend1(String friend1) {
        this.friend1 = friend1;
    }

    public String getFriend2() {
        return friend2;
    }

    public void setFriend2(String friend2) {
        this.friend2 = friend2;
    }
}
