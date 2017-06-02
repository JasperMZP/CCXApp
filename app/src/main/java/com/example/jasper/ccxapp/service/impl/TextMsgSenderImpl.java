package com.example.jasper.ccxapp.service.impl;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.ShowMsgSender;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Jasper on 2017/6/2.
 */

public class TextMsgSenderImpl implements ShowMsgSender {
    @Override
    public void sendMsg(ShowItemModel showItemForSend, Conversation mConversation) {
        TextContent textContent = new TextContent(showItemForSend.getShowText());
        textContent.setStringExtra("showKey", showItemForSend.getMsgKey());
        String groupIds = "";
        for (long groupId : showItemForSend.getGroupBelongToList()) {
            groupIds += groupId + ",";
        }
        textContent.setStringExtra("groupBelongTo", groupIds);
        Message message = mConversation.createSendMessage(textContent);
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //消息发送成功
                    Log.i("test", "文本发送成功");
                } else {
                    //消息发送失败
                    Log.e("test", "文本发送失败");
                }
            }
        });

        JMessageClient.sendMessage(message);
    }
}
