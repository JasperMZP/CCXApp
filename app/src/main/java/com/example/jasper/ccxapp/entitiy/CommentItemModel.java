package com.example.jasper.ccxapp.entitiy;

/**
 * Created by Jasper on 2017/4/19.
 */

public class CommentItemModel {
    private String msgKey;
    private String commKey;
    private String commentUsername;
    private String commentVoice;
    private int commentLength;

    public String getCommKey() {
        return commKey;
    }

    public void setCommKey(String commKey) {
        this.commKey = commKey;
    }

    public int getCommentLength() {
        return commentLength;
    }

    public void setCommentLength(int commentLength) {
        this.commentLength = commentLength;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(String commentUsername) {
        this.commentUsername = commentUsername;
    }

    public String getCommentVoice() {
        return commentVoice;
    }

    public void setCommentVoice(String commentVoice) {
        this.commentVoice = commentVoice;
    }
}
