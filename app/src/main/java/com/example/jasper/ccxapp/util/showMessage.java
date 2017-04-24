package com.example.jasper.ccxapp.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.ui.NewFriendActivity;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class showMessage {
    public static void showNewFriend(Activity activity, String title, String content){
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        Notification.Builder builder1 = new Notification.Builder(activity);
        builder1.setSmallIcon(R.drawable.logo); //设置图标
        builder1.setTicker("新的好友通知");
        builder1.setContentTitle(title); //设置标题
        builder1.setContentText(content); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (activity, NewFriendActivity.class);
        activity.finish();
        PendingIntent pendingIntent =PendingIntent.getActivity(activity, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        notificationManager.notify(124, notification1); // 通过通知管理器发送通知
    }
}
