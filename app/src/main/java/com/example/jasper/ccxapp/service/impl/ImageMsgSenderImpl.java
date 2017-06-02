package com.example.jasper.ccxapp.service.impl;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.ShowMsgSender;
import com.example.jasper.ccxapp.util.UUIDKeyUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Jasper on 2017/6/2.
 */

public class ImageMsgSenderImpl implements ShowMsgSender {
    @Override
    public void sendMsg(ShowItemModel showItemForSend, Conversation mConversation) {
        ArrayList<String> imgPaths = showItemForSend.getShowImagesList();
        String recieveFlag = UUIDKeyUtil.getUUIDKey();
        for (int i = 0; i < imgPaths.size(); i++) {
            try {
                ImageContent imageContent = new ImageContent(new File(imgPaths.get(i)));
                Map<String,String> extrasMap = new HashMap<String,String>();
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
}
