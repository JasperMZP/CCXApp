package com.example.jasper.ccxapp.service.impl;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.ShowMsgSender;

import java.io.File;
import java.io.FileNotFoundException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Jasper on 2017/6/2.
 */

public class VideoMsgSenderImpl implements ShowMsgSender {
    @Override
    public void sendMsg(ShowItemModel showItemForSend, Conversation mConversation) {
        try {
            FileContent fileContent = new FileContent(new File(showItemForSend.getShowVideo()));
            fileContent.setStringExtra("showKey", showItemForSend.getMsgKey());
            fileContent.setStringExtra("showText", showItemForSend.getShowText());
            String groupIds = "";
            for (long groupId : showItemForSend.getGroupBelongToList()) {
                groupIds += groupId + ",";
            }
            fileContent.setStringExtra("groupBelongTo", groupIds);
            Message message = mConversation.createSendMessage(fileContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "视频发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JMFileSizeExceedException e) {
            e.printStackTrace();
        }
    }
}
