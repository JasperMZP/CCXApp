package com.example.jasper.ccxapp.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by Jasper on 2017/3/21.
 */

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this, "11c7be8b5fad9ba573d2641f495dbc48");//初始化Bmob
    }
}
