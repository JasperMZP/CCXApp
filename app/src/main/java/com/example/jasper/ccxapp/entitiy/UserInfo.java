package com.example.jasper.ccxapp.entitiy;


import java.io.File;

import cn.jpush.im.android.api.callback.DownloadAvatarCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class UserInfo extends cn.jpush.im.android.api.model.UserInfo {

    @Override
    public String getNotename() {
        return notename;
    }

    @Override
    public String getNoteText() {
        return noteText;
    }

    @Override
    public long getBirthday() {
        return Long.valueOf(birthday);
    }

    @Override
    public File getAvatarFile() {
        File avatar = new File(avatarMediaID);
        return avatar;
    }

    @Override
    public void getAvatarFileAsync(DownloadAvatarCallback downloadAvatarCallback) {

    }

    @Override
    public void getAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public void getBigAvatarBitmap(GetAvatarBitmapCallback getAvatarBitmapCallback) {

    }

    @Override
    public int getBlacklist() {
        return 0;
    }

    @Override
    public int getNoDisturb() {
        return 0;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public String getAppKey() {
        return null;
    }

    @Override
    public void setBirthday(long l) {
        birthday = String.valueOf(l);
    }

    @Override
    public void setNoDisturb(int i, BasicCallback basicCallback) {

    }

    @Override
    public void removeFromFriendList(BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteName(String s, BasicCallback basicCallback) {

    }

    @Override
    public void updateNoteText(String s, BasicCallback basicCallback) {

    }
}
