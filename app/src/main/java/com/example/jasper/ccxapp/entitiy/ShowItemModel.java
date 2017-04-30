package com.example.jasper.ccxapp.entitiy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 2017/4/19.
 */

public class ShowItemModel implements Serializable {
    private List<Long> groupBelongToList;
    private String msgKey;
    private String showUsername;
    private String showText;
    private ArrayList<String> showImagesList;
    private String showVideo;

    public List<Long> getGroupBelongToList() {
        return groupBelongToList;
    }

    public void setGroupBelongToList(List<Long> groupBelongToList) {
        this.groupBelongToList = groupBelongToList;
    }

    public ArrayList<String> getShowImagesList() {
        return showImagesList;
    }

    public void setShowImagesList(ArrayList<String> showImagesList) {
        this.showImagesList = showImagesList;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getShowUsername() {
        return showUsername;
    }

    public void setShowUsername(String showUsername) {
        this.showUsername = showUsername;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }


    public String getShowVideo() {
        return showVideo;
    }

    public void setShowVideo(String showVideo) {
        this.showVideo = showVideo;
    }
}
