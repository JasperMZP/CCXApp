package com.example.jasper.ccxapp.interfaces;

import com.example.jasper.ccxapp.entitiy.ShowItemModel;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Created by Jasper on 2017/6/2.
 */

public interface ShowMsgSender {
    public void sendMsg(ShowItemModel showItemForSend, Conversation mConversation);
}
