package com.example.jasper.ccxapp.entitiy;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by DPC on 2017/5/10.
 */
public class ShowItemModelTest {
    private String msgKey="a";
    private String showUsername="b";
    private String showText="c";
    private String showVideo="d";
    private File showAvatar=new File("F:/abc.txt");
    @Test
    public void testgetShowAvatar() throws Exception {
        assertEquals(showAvatar.getName(),"abc.txt");
    }

    @Test
    public void testsetShowAvatar() throws Exception {
        File s = new File("F:/bcd.txt");
        showAvatar=s;
        assertEquals(showAvatar.getName(),"bcd.txt");

    }

    @Test
    public void testgetMsgKey() throws Exception {
        assertEquals("a",msgKey);
    }

    @Test
    public void testrsetMsgKey() throws Exception {
        String msgkey ="aaa";
        this.msgKey=msgkey;
        assertEquals("aaa",this.msgKey);
    }

    @Test
    public void testgetShowUsername() throws Exception {
        assertEquals("b",showUsername);
    }

    @Test
    public void testsetShowUsername() throws Exception {
        String showusername= "bbb";
        this.showUsername= showusername;
        assertEquals("bbb",this.showUsername);
    }

    @Test
    public void testgetShowText() throws Exception {
        assertEquals("c",showText);
    }

    @Test
    public void testsetShowText() throws Exception {
        String showtext = "fff";
        this.showText= showtext;
        assertEquals("fff",this.showText);
    }

    @Test
    public void testgetShowVideo() throws Exception {
        assertEquals("d",showVideo);
    }

    @Test
    public void testsetShowVideo() throws Exception {
        String showvideo = "ddd";
        this.showVideo=showvideo;
        assertEquals("ddd",this.showVideo);
    }

}