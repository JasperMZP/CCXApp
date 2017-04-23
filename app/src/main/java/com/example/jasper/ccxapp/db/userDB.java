package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entities.User;
import com.example.jasper.ccxapp.entities.UserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class userDB {

    private static UserInfo userInfo;

    //需输入用户名，密码新建用户
    public static void addNewUser(String userName, String pwd, final userBackListener ubl) {
        JMessageClient.register(userName, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(s.equals("Success")) {
                    ubl.showResult(true, "");
                }else{
                    ubl.showResult(false, s);
                }
            }
        });
    }

    //判断是否成功登陆
    public static void forUserLogin(String userName, final String password, final userBackListener ubl) {
        JMessageClient.login(userName, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    ubl.showResult(true, "");
                }else{
                    ubl.showResult(false, s);
                }
            }
        });
    }

    //添加用户相关信息
    public static void addUserMessage(String userName, String nickName, String sex,
            String birthday, String address, String explain, final userBackListener ubl) {
//        UserInfo.Field field = new UserInfo.Field();
        cn.jpush.im.android.api.model.UserInfo.Field field = null;

        UserInfo userInfo = new UserInfo();
        userInfo.setAddress(address);
        JMessageClient.updateMyInfo(field.address, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.i("user", "aaaaaaaaaaaa"+i+"    "+s);
                if(i == 0){
                    ubl.showResult(true, "");
                }else{
                    ubl.showResult(false, s);
                }
            }
        });
    }

    private static User getUser(String userName, String password) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        return user;
    }
}
