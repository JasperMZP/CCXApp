package com.example.jasper.ccxapp.entitiy;

/**
 * Created by Jasper on 2017/4/19.
 */

public class CommentItemModel {
    private String msgKey;
    private String commentUsername;
    private String commentVoice;
    private String commentLength;

    public String getCommentLength() {
        return commentLength;
    }

    public void setCommentLength(String commentLength) {
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
