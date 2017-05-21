package com.example.jasper.ccxapp.interfaces;

/**
 * Created by Jasper on 2017/5/20.
 */

public interface MessageDBSQL {

    String MESSAGE_DB_NAME = "Message.db";

    int DATABASE_VERSION = 1;

    String showTextTableName = "ShowTextTable";
    String showImageTableName = "ShowImageTable";
    String showVideoTableName = "ShowVideoTable";
    String commentTableName = "CommentTable";

    /**
     * private String msgKey;
     private List<Long> groupBelongToList;
     private String showUsername;
     private File showAvatar;
     private String showText;
     private String showTime;
     private ArrayList<String> showImagesList;
     private String showVideo;
     */

    String CREATE_SHOW_TEXT = "create table if not exists "+showTextTableName+" (" +
            "msgKey text primary key," +
            "groupBelongToList text," +
            "showUsername text," +
            "showAvatar text," +
            "showTime text," +
            "showText text"+
            ");";

    String CREATE_SHOW_IMAGE = "create table if not exists "+showImageTableName+" (" +
            "msgKey text primary key," +
            "groupBelongToList text," +
            "showUsername text," +
            "showAvatar text," +
            "showTime text," +
            "showText text,"+
            "showImagesList text" +
            ");";

    String CREATE_SHOW_VIDEO = "create table if not exists "+showVideoTableName+" (" +
            "msgKey text primary key," +
            "groupBelongToList text," +
            "showUsername text," +
            "showAvatar text," +
            "showTime text," +
            "showText text,"+
            "showVideo text" +
            ");";

    /**
     * private String msgKey;
     private String commKey;
     private String commentUsername;
     private String commentVoice;
     private int commentLength;
     private String commentTime;
     */

    String CREATE_COMMENT ="create table if not exists "+commentTableName+" (" +
            "commKey text primary key," +
            "msgKey text," +
            "commentUsername text," +
            "commentVoice text," +
            "commentLength integer," +
            "commentTime text" +
            ")";

}
