package com.example.jasper.ccxapp.db;


import com.example.jasper.ccxapp.entitiy.SecurityData;
import com.example.jasper.ccxapp.entitiy.User;
import com.example.jasper.ccxapp.entitiy.UserInfo;
import com.example.jasper.ccxapp.interfaces.UserBackListListener;
import com.example.jasper.ccxapp.interfaces.UserBackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class userDB {

    //需输入用户名，密码新建用户
    public static void addNewUser(final String userName, final String pwd, final UserBackListener ubl) {
        JMessageClient.register(userName, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    User user = new User();
                    user.setUserName(userName);
                    user.setPassword(pwd);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId,BmobException e) {
                            if(e==null){
                                ubl.showResult(true, "");
                            }else{
                                ubl.showResult(false, e.getMessage());
                            }
                        }
                    });
                } else {
                    ubl.showResult(false, s);
                }
            }
        });
    }

    //判断是否成功登陆
    public static void forUserLogin(String userName, final String password, final UserBackListener ubl) {
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

    //修改用户密码
    public static void changeUserPwd(final String userName, final String password, final UserBackListener ubl) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("userName", userName);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(final List<User> list, BmobException e) {
                if (e == null) {
                    JMessageClient.login(userName, list.get(0).getPassword(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if(i==0){
                                JMessageClient.updateUserPassword(list.get(0).getPassword(), password, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if(i==0){
                                            User user = list.get(0);
                                            user.setPassword(password);
                                            user.update(user.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        ubl.showResult(true, "");
                                                    } else {
                                                        ubl.showResult(false, null);
                                                    }
                                                }
                                            });
                                        } else {
                                            ubl.showResult(false, null);
                                        }
                                    }
                                });
                            } else {
                                ubl.showResult(false, null);
                            }
                        }
                    });
                } else {
                    ubl.showResult(false, null);
                }
            }
        });
    }

    //添加用户密保信息
    public static void addSecurityQA(String Q1, String A1, String Q2, String A2, final UserBackListener ubl) {
        SecurityData s = new SecurityData();
        s.setUserName(JMessageClient.getMyInfo().getUserName());
        s.setA1(A1);
        s.setA2(A2);
        s.setQ1(Q1);
        s.setQ2(Q2);
        s.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    ubl.showResult(true, "");
                }else{
                    ubl.showResult(false, e.getMessage());
                }
            }
        });
    }

    //获得用户密保信息
    public static void getSecurityQA(String userName, final UserBackListListener ubl) {
        BmobQuery<SecurityData> query = new BmobQuery<SecurityData>();
        query.addWhereEqualTo("userName", userName);
        query.findObjects(new FindListener<SecurityData>() {
            @Override
            public void done(final List<SecurityData> list, BmobException e) {
                if (e == null) {
                    if(list.size() == 0){
                        ubl.showResult(false, null, null);
                    }else {
                        ArrayList<String> messages = new ArrayList<String>();
                        messages.add(list.get(0).getQ1());
                        messages.add(list.get(0).getA1());
                        messages.add(list.get(0).getQ2());
                        messages.add(list.get(0).getA2());
                        ubl.showResult(true, messages, null);
                    }
                } else {
                    ubl.showResult(false, null, null);
                }
            }
        });
    }

    //添加用户相关信息
    public static void addUserMessage(File image, String nickName, cn.jpush.im.android.api.model.UserInfo.Gender sex, Long birthday, String address, String explain, final UserBackListener ubl) {
        final int[] a = {0, 0};
        if (image != null && image.exists()) {
            a[0]++;
            JMessageClient.updateUserAvatar(image, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
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
    public static void addUserIdentity(String identity, final UserBackListener ubl) {
        UserInfo userInfo = new UserInfo();
        userInfo.setRegion(identity);
        JMessageClient.updateMyInfo(UserInfo.Field.region, userInfo, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i != 0) {
                        ubl.showResult(false, s);
                    } else {
                        ubl.showResult(true, "");
                    }
                }
        });
    }

}
