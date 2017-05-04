package com.example.jasper.ccxapp.interfaces;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public interface userBackListListener {
    public void showResult(boolean result, ArrayList<String> message, List<UserInfo> userInfos);
}
