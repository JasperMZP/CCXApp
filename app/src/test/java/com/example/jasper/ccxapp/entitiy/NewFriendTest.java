package com.example.jasper.ccxapp.entitiy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DPC on 2017/5/10.
 */
public class NewFriendTest {
    private String requestFriend="yes";
    private String responseFriend="get";
    private String message="hello";

    @Test
    public void testgetRequestFriend() throws Exception {
        assertEquals(requestFriend,"yes");
    }

    @Test
    public void testsetRequestFriend() throws Exception {
        String requestFriend = "no";
        this.requestFriend =requestFriend;
        assertEquals("no",this.requestFriend);
    }

    @Test
    public void testgetResponseFriend() throws Exception {
        assertEquals(responseFriend,"get");
    }

    @Test
    public void testsetResponseFriend() throws Exception {
        String responseFriend ="Noget";
        this.responseFriend = responseFriend;
        assertEquals("Noget",this.responseFriend);
    }

    @Test
    public void testgetMessage() throws Exception {
        assertEquals("hello",message);
    }

    @Test
    public void testsetMessage() throws Exception {
        String message = "what";
        this.message = message;
        assertEquals("what",this.message);

    }

}