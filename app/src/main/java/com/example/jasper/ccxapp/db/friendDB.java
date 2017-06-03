package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entitiy.NewFriend;
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;
import com.example.jasper.ccxapp.interfaces.userBackUserInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class friendDB {
    //拒绝好友申请，删除后台中对应好友申请的部分
    public static void disagreefriend(String userName1, final String userName2, final userBackListener userBackListener) {
        BmobQuery<NewFriend> query = new BmobQuery<NewFriend>();
        query.addWhereEqualTo("requestFriend", userName2);
        query.addWhereEqualTo("responseFriend", userName1);
        query.findObjects(new FindListener<NewFriend>() {
            @Override
            public void done(List<NewFriend> list, BmobException e) {
                if(e==null){
                    for(NewFriend friend : list){
                        friend.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ContactManager.declineInvitation(userName2, null, "", new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if(0 == i){
                                                userBackListener.showResult(true, "");
                                            }else {
                                                userBackListener.showResult(false, s);
                                            }
                                        }
                                    });
                                    Log.d("friend", "删除信息成功");
                                }else{
                                    Log.e("friend", "删除信息失败"+e.getMessage());
                                }
                            }
                        });
                    }
                }else{
                    userBackListener.showResult(false, e.getMessage());
                    Log.e("friend", "删除信息失败"+e.getMessage());
                }
            }
        });
    }

    //同意好友申请，删去申请表中的对应信息，在好友表中添加相应好友信息
    public static void agreenewfriend(final  String userName1,final String userName2,final userBackListener userBackListener) {
        BmobQuery<NewFriend> query = new BmobQuery<NewFriend>();
        query.addWhereEqualTo("requestFriend", userName2);
        query.addWhereEqualTo("responseFriend", userName1);
        query.findObjects(new FindListener<NewFriend>() {
            @Override
            public void done(List<NewFriend> list, BmobException e) {
                if(e==null){
                    for(NewFriend friend : list){
                        friend.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ContactManager.acceptInvitation(userName2, null, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if(0 == i){
                                                userBackListener.showResult(true, "");
                                            }else {
                                                userBackListener.showResult(false, s);
                                            }
                                        }
                                    });
                                    Log.d("friend", "删除信息成功");
                                }else{
                                    Log.e("friend", "删除信息失败"+e.getMessage());
                                }
                            }
                        });
                    }
                }else{
                    userBackListener.showResult(false, e.getMessage());
                    Log.e("friend", "删除信息失败"+e.getMessage());
                }
            }
        });
    }

    //发送好友请求,userName2为接受请求人
    public static void sendfriendrequest(final String userName2, final String message, final userBackListener userBackListener) {
        BmobQuery<NewFriend> query = new BmobQuery<NewFriend>();
        query.addWhereEqualTo("requestFriend", userName2);
        query.addWhereEqualTo("responseFriend", JMessageClient.getMyInfo().getUserName());
        query.findObjects(new FindListener<NewFriend>() {
            @Override
            public void done(List<NewFriend> list, BmobException e) {
                if(e==null){
                    if(list.size() == 0){
                        NewFriend friend = getNewFriend(JMessageClient.getMyInfo().getUserName(), userName2, message);
                        friend.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    ContactManager.sendInvitationRequest(userName2, null, message, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if(0 == i){
                                                userBackListener.showResult(true, "");
                                            }else {
                                                userBackListener.showResult(false, s);
                                            }
                                        }
                                    });
                                    Log.d("friend", "发送好友请求成功");
                                }else{
                                    userBackListener.showResult(false, e.getMessage());
                                    Log.e("friend", "发送好友请求失败：" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        for(NewFriend friend : list){
                            friend.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        ContactManager.acceptInvitation(userName2, null, new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                if(0 == i){
                                                    userBackListener.showResult(true, "");
                                                }else {
                                                    userBackListener.showResult(false, s);
                                                }
                                            }
                                        });
                                        Log.d("friend", "添加好友成功");
                                    }else{
                                        Log.e("friend", "添加好友失败"+e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }else{
                    userBackListener.showResult(false, e.getMessage());
                    Log.e("friend", "添加好友失败"+e.getMessage());
                }
            }
        });
    }

    //查询当前好友
    public static void searchfriend(final userBackListUserInfo userBackListUserInfo) {
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                if (0 == responseCode) {
                    userBackListUserInfo.showResult(true, userInfoList);
                } else {
                    userBackListUserInfo.showResult(false, null);
                }
            }
        });
    }

    //查询新好友
    public static void searchnewfriend(final String userName2, final userBackUserInfo userBackUserInfo) {
        JMessageClient.getUserInfo(userName2,new GetUserInfoCallback(){
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(i == 0){
                    Log.i("test","用户信息获取成功");
                    if(userInfo.isFriend()){
                        userBackUserInfo.showResult(false, "已加该用户为好友", null);
                    }else{
                        userBackUserInfo.showResult(true, "", userInfo);
                    }
                }else{
                    userBackUserInfo.showResult(false, "查找不到相应的用户", null);
                }
            }
        });
    }

    //删除已有好友输入值为好友双方
    public static void deletefriend(UserInfo userInfo, final userBackListener userBackListener) {
        userInfo.removeFromFriendList(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (0 == responseCode) {
                    userBackListener.showResult(true, null);
                } else {
                    userBackListener.showResult(false, responseMessage);
                }
            }
        });
    }

    //得到该用户被请求为好友的信息
    public static void searchRequestList(String userName, final userBackListListener userBackListListener) {
        final List<UserInfo> userInfos = new ArrayList<UserInfo>();
        BmobQuery<NewFriend> query = new BmobQuery<NewFriend>();
        query.addWhereEqualTo("responseFriend", userName);
        query.setLimit(200);
        query.findObjects(new FindListener<NewFriend>() {
            @Override
            public void done(final List<NewFriend> list, BmobException e) {
                final ArrayList<String> messages = new ArrayList<String>();
                if(e==null){
                    for(final NewFriend friend : list) {
                        JMessageClient.getUserInfo(friend.getRequestFriend(), new GetUserInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, UserInfo userInfo) {
                                if(i == 0){
                                    userInfos.add(userInfo);
                                    messages.add(friend.getMessage());
                                }else{
                                    userBackListListener.showResult(false, null, null);
                                    return;
                                }
                                if(messages.size() == list.size()){
                                    userBackListListener.showResult(true, messages, userInfos);
                                }
                            }
                        });
                    }
                    if(list.size() == 0){
                        userBackListListener.showResult(true, messages, userInfos);
                    }
                }else {
                    Log.e("friend", "查询信息失败" + e.getMessage());
                    messages.add(e.getMessage());
                    userBackListListener.showResult(false, messages, null);
                }
            }
        });
    }


    //构建NewFriend类
    private static NewFriend getNewFriend(String userName1, String userName2, String message) {
        NewFriend friend = new NewFriend();
        friend.setRequestFriend(userName1);
        friend.setResponseFriend(userName2);
        friend.setMessage(message);
        return friend;
    }
}
