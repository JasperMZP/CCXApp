package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entities.User;
import com.example.jasper.ccxapp.utils.QueryUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.UUID;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jasper on 2017/3/24.
 */

public class SignUpObj {
    static User user;
    public static void CreateUser(String username,String password,String userId) {
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //user.setId(UUID.randomUUID().toString());//给用户生成唯一的
        user.setId(userId);
        user.save(new SaveListener<String>() {
            @Override
            public void done(final String objectId, BmobException e) {
                if (e == null) {
                    //Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                    Log.d("signup","Bmob添加数据成功，返回objectId为：" + objectId);
                   SignUpObj.signUpToEase();

                } else {
                    //Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("signup", "Bmob创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 将用户注册并绑定到环信
     */
    public static void signUpToEase(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(user.getId(),user.getPassword());
                    Log.e("easemob", "注册成功");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("easemob", "注册失败" + e.getErrorCode() + e.getMessage());
                }
            }
        }).start();
    }
}
