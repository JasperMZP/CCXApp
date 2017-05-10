package com.example.jasper.ccxapp.entitiy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DPC on 2017/5/10.
 */
public class CommentItemModelTest {

    private String msgKey="a";
    private String commKey="b";
    private String commentUsername="c";
    private String commentVoice="d";
    private int commentLength=1;

    @Test
    public void testgetCommKey() throws Exception {
            assertEquals(commKey,"b");
    }

    @Test
    public void testsetCommKey() throws Exception {
       int  commentLength = 2;
        this.commentLength = commentLength;
            assertEquals(2,this.commentLength);
    }

    @Test
    public void testgetCommentLength() throws Exception {
            assertEquals(commentLength,1);
    }

    @Test
    public void testsetCommentLength() throws Exception {
            String commentUsername ="dd";
            this.commentUsername=commentUsername;
            assertEquals("dd",this.commentUsername);
    }

    @Test
    public void testgetMsgKey() throws Exception {
            assertEquals(msgKey,"a");
    }

    @Test
    public void setMsgKey() throws Exception {
           String msgKey = "e";
            this.msgKey=msgKey;
            assertEquals("e",this.msgKey);
    }

    @Test
    public void testgetCommentUsername() throws Exception {
        assertEquals("c",commentUsername);
    }

    @Test
    public void testsetCommentUsername() throws Exception {
       String  commentUsername = "ee";
        this.commentUsername=commentUsername;
        assertEquals("ee",this.commentUsername);
    }

    @Test
    public void testgetCommentVoice() throws Exception {
        assertEquals("d",commentVoice);
    }

    @Test
    public void testsetCommentVoice() throws Exception {
       String commentVoice="ff";
        this.commentVoice=commentVoice;
        assertEquals("ff",this.commentVoice);
    }

}