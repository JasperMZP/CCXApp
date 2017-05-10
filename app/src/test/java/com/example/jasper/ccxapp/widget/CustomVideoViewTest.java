package com.example.jasper.ccxapp.widget;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DPC on 2017/5/10.
 */
public class CustomVideoViewTest {
    private int videoWidth=10;
    private int videoHeight=20;
    @Test
    public void testgetVideoWidth() throws Exception {
    assertEquals(videoWidth,10);
    }

    @Test
    public void setVideoWidth() throws Exception {
    this.videoWidth=20;
        assertEquals(20,this.videoWidth);
    }

    @Test
    public void getVideoHeight() throws Exception {
    assertEquals(20,videoHeight);
    }

    @Test
    public void setVideoHeight() throws Exception {
        this.videoHeight=30;
        assertEquals(30,this.videoHeight);
    }

}