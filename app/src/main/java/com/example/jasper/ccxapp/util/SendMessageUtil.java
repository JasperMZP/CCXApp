package com.example.jasper.ccxapp.util;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.CommentItemModel;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Jasper on 2017/5/9.
 */

public class SendMessageUtil {
    public static void sendTextMsg(ShowItemModel showItemForSend, Conversation mConversation) {

    }

    public static void sendImageMsg(ShowItemModel showItemForSend,Conversation mConversation) {

    }


    public static void sendVideoMsg(ShowItemModel showItemForSend,Conversation mConversation) {

    }

    public static void sendVoice(CommentItemModel commentItemForSend,Conversation mConversation) {
        try {
            VoiceContent voiceContent = new VoiceContent(new File(commentItemForSend.getCommentVoice()), commentItemForSend.getCommentLength());
            Map<String,String> extrasMap = new HashMap<String,String>();
            extrasMap.put("showKey", commentItemForSend.getMsgKey());
            extrasMap.put("commKey", commentItemForSend.getCommKey());
            extrasMap.put("voiceLength", "" + commentItemForSend.getCommentLength());
            voiceContent.setExtras(extrasMap);
            Message message = mConversation.createSendMessage(voiceContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "发送语音消息" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
