package com.example.jasper.ccxapp.interfaces;

/**
 * Created by Jasper on 2017/5/20.
 */

public interface MessageDBSQL {

    String MESSAGE_DB_NAME = "Message.db";

    String CREATE_SHOW_TEXT = "create table ShowMessage (" +
            "msgKey text primary key" +
            "groupBelongToList text" +
            "showUsername text" +
            "showAvatar text" +
            "showTime text" +
            "showText text"+
            ")";

    String CREATE_SHOW_IMAGE = "create table ShowMessage (" +
            "msgKey text primary key" +
            "groupBelongToList text" +
            "showUsername text" +
            "showAvatar text" +
            "showTime text" +
            "showText text"+
            "showImagesList text" +
            ")";

    String CREATE_SHOW_VIDEO = "create table ShowMessage (" +
            "msgKey text primary key" +
            "groupBelongToList text" +
            "showUsername text" +
            "showAvatar text" +
            "showTime text" +
            "showText text"+
            "showVideo text" +
            ")";

    /**
     * private String msgKey;
     private String commKey;
     private String commentUsername;
     private String commentVoice;
     private int commentLength;
     private String commentTime;
     */

    String CREATE_COMMENT ="create table Comment (" +
            "msgKey text" +
            "commKey text" +
            "commentUsername text" +
            "commentVoice text" +
            "commentLength integer" +
            "commentTime text" +
            ")";

}
