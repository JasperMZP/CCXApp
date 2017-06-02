package com.example.jasper.ccxapp.service.factory;

import com.example.jasper.ccxapp.interfaces.MessageType;
import com.example.jasper.ccxapp.interfaces.ShowMsgSender;
import com.example.jasper.ccxapp.service.impl.ImageMsgSenderImpl;
import com.example.jasper.ccxapp.service.impl.TextMsgSenderImpl;
import com.example.jasper.ccxapp.service.impl.VideoMsgSenderImpl;


/**
 * Created by Jasper on 2017/6/2.
 */

public class SendFactory implements MessageType{

    public ShowMsgSender ShowMsgSenderProduce(String type){
        if (type==SHOW_TEXT){
            return new TextMsgSenderImpl();
        }else if (type==SHOW_IMAGE){
            return new ImageMsgSenderImpl();
        }else if (type==SHOW_VIDEO){
            return new VideoMsgSenderImpl();
        }else {
            return null;
        }
    }
}
