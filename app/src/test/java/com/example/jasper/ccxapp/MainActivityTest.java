package com.example.jasper.ccxapp;

import android.app.usage.UsageEvents;
import android.provider.ContactsContract;
import android.util.EventLog;
import cn.jpush.im.android.api.event.MessageEvent;
import com.example.jasper.ccxapp.ui.MainActivity;

import org.junit.Test;



import static org.junit.Assert.*;

/**
 * Created by 彤彤 on 2017/4/8.
 */
public class MainActivityTest {
    @Test
    public void onActivityResult() throws Exception {
        MessageEvent event = null;
        MainActivity main = new MainActivity();
        main.onEventMainThread(event);
    }

}