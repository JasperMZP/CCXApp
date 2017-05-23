package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.UserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class userDB {

    //需输入用户名，密码新建用户
    public static void addNewUser(String userName, String pwd, final userBackListener ubl) {
        JMessageClient.register(userName, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    ubl.showResult(true, "");
                } else {
                    ubl.showResult(false, s);
                }
            }
        });
    }

    //判断是否成功登陆
    public static void forUserLogin(String userName, final String password, final userBackListener ubl) {
        JMessageClient.login(userName, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    ubl.showResult(true, "");
                } else {
                    ubl.showResult(false, s);
                }
            }
        });
    }

    //添加用户相关信息
    public static void addUserMessage(File image, String nickName, cn.jpush.im.android.api.model.UserInfo.Gender sex, Long birthday, String address, String explain, final userBackListener ubl) {
        final int[] a = {0, 0};
        if (image != null && image.exists()) {
            a[0]++;
            JMessageClient.updateUserAvatar(image, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaafile" + i + "    " + s);
                    if (i != 0) {
                        ubl.showResult(false, s);
                        a[1]++;
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(nickName);
        userInfo.setGender(sex);
        userInfo.setAddress(address);
        userInfo.setSignature(explain);
        if (nickName != null) {
            a[0]++;
            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaanickname" + i + "    " + s);
                    if (i != 0) {
                        if (a[1] == 0) {
                            ubl.showResult(false, s);
                            a[1]++;
                        }
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }
        if (sex != null) {
            a[0]++;
            JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaagender" + i + "    " + s);
                    if (i != 0) {
                        if (a[1] == 0) {
                            ubl.showResult(false, s);
                            a[1]++;
                        }
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }
        if (birthday != null) {
            a[0]++;
            userInfo.setBirthday(birthday);
            JMessageClient.updateMyInfo(UserInfo.Field.birthday, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaabirthday" + i + "    " + s);
                    if (i != 0) {
                        if (a[1] == 0) {
                            ubl.showResult(false, s);
                            a[1]++;
                        }
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }
        if (address != null) {
            a[0]++;
            JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaaaddress" + i + "    " + s);
                    if (i != 0) {
                        if (a[1] == 0) {
                            ubl.showResult(false, s);
                            a[1]++;
                        }
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }

        if (explain != null) {
            a[0]++;
            JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaasignture" + i + "    " + s);
                    if (i != 0) {
                        if (a[1] == 0) {
                            ubl.showResult(false, s);
                            a[1]++;
                        }
                    } else {
                        a[0]--;
                        if (a[0] == 0) {
                            ubl.showResult(true, "");
                        }
                    }
                }
            });
        }
    }

    //添加用户相关信息
    public static void addUserIdentity(String identity, final userBackListener ubl) {
        UserInfo userInfo = new UserInfo();
        userInfo.setRegion(identity);
        JMessageClient.updateMyInfo(UserInfo.Field.region, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("user", "aaaaaaaaaaaaidentity" + i + "    " + s);
                    if (i != 0) {
                        ubl.showResult(false, s);
                    } else {
                        ubl.showResult(true, "");
                    }
                }
        });
    }

}
