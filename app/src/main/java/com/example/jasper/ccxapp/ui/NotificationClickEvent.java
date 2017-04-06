package com.example.jasper.ccxapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Jasper on 2017/4/6.
 */
/*

public class NotificationClickEvent extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }
    public void onEvent(NotificationClickEvent event){
        Intent notificationIntent = new Intent(BaseApplication.getContext(), MainActivity.class);
        BaseApplication.getContext().startActivity(notificationIntent);//自定义跳转到指定页面
    }
}
*/
