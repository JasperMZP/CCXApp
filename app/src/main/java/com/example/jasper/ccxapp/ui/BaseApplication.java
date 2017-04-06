package com.example.jasper.ccxapp.ui;


import android.app.Application;


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
