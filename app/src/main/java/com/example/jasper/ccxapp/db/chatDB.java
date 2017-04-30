package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class chatDB {
    //添加新聊天室
    public static void addnewchatroom(String userName, String chatroom, final ArrayList<String> otherusers, final userBackListener userBackListener) {
        JMessageClient.createGroup(chatroom, "咱一家人的描述", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, final long l) {
                Log.i("test", "创建群聊" + "a" + i + " b" + s + " c" + l);
                final long groupId = l;

                JMessageClient.addGroupMembers(groupId, otherusers, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test", "添加群成员" + i + s);
                        if(i == 0) {
                            userBackListener.showResult(true, "");
                        }else {
                            userBackListener.showResult(false, s);
                        }
                    }
                });
            }
        });
    }
}
