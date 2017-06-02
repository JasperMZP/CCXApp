package com.example.jasper.ccxapp.entitiy;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class SecurityData extends BmobObject {
    private String Q1;
    private String A1;
    private String Q2;
    private String A2;
    private String userName;

    public String getQ1() {
        return Q1;
    }

    public void setQ1(String q1) {
        Q1 = q1;
    }

    public String getA1() {
        return A1;
    }

    public void setA1(String a1) {
        A1 = a1;
    }

    public String getQ2() {
        return Q2;
    }

    public void setQ2(String q2) {
        Q2 = q2;
    }

    public String getA2() {
        return A2;
    }

    public void setA2(String a2) {
        A2 = a2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
