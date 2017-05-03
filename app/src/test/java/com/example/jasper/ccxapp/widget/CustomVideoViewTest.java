package com.example.jasper.ccxapp.widget;

import org.junit.Test;

import static org.junit.Assert.*;
import com.example.jasper.ccxapp.widget.CustomVideoView;

public class CustomVideoViewTest {
    private int videoWidth = 1;
    private int videoHeight = 2;

    @Test
    public void getVideoWidthTest() throws Exception {
        assertEquals(videoWidth, 1);
    }

    @Test
    public void setVideoWidth(int videoWidth) throws Exception {
        videoWidth = 1;
        assertEquals(this.videoWidth, videoWidth);
    }

    @Test
    public void getVideoHeight() throws Exception {
        assertEquals(videoHeight, 2);
    }

    @Test
    public void setVideoHeight(int videoHeight) throws Exception {
        videoHeight = 2;
        assertEquals(this.videoHeight, videoHeight);

    }
}