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

    public static void sendImageMsg(ShowItemModel showItemForSend,Conversation mConversation) {
        ArrayList<String> imgPaths = showItemForSend.getShowImagesList();
        String recieveFlag = UUIDKeyUtil.getUUIDKey();
        for (int i = 0; i < imgPaths.size(); i++) {
            try {
                ImageContent imageContent = new ImageContent(new File(imgPaths.get(i)));
                Map extrasMap = new HashMap();
                extrasMap.put("showKey", showItemForSend.getMsgKey());
                if (i == 0 && showItemForSend.getShowText() != null) {
                    extrasMap.put("showText", showItemForSend.getShowText());
                }
                String groupIds = "";
                for (long groupId : showItemForSend.getGroupBelongToList()) {
                    groupIds += groupId + ",";
                }
                Log.i("test", "传递groupIds" + groupIds);
                //imageContent.setStringExtra("groupBelongTo",groupIds);
                extrasMap.put("groupBelongTo", groupIds);
                imageContent.setExtras(extrasMap);
                imageContent.setNumberExtra("imageNum", imgPaths.size());
                imageContent.setNumberExtra("imageCount", i + 1);
                imageContent.setStringExtra("recieveFlag", recieveFlag);
                Message message = mConversation.createSendMessage(imageContent);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test", "发送图片消息" + i + s);
                    }
                });
                JMessageClient.sendMessage(message);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static void sendVideoMsg(ShowItemModel showItemForSend,Conversation mConversation) {
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

    public static void sendVoice(CommentItemModel commentItemForSend,Conversation mConversation) {
        try {
            VoiceContent voiceContent = new VoiceContent(new File(commentItemForSend.getCommentVoice()), commentItemForSend.getCommentLength());
            Map extrasMap = new HashMap();
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
