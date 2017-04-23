package com.example.jasper.ccxapp;

import cn.jpush.im.android.api.event.MessageEvent;
import com.example.jasper.ccxapp.ui.MainActivity3;

import org.junit.Test;

/**
 * Created by 彤彤 on 2017/4/8.
 */
public class MainActivityTest {
    @Test
    public void onActivityResult() throws Exception {
        MessageEvent event = null;
        MainActivity3 main = new MainActivity3();
        main.onEventMainThread(event);
    }

}