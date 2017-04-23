package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entities.Friend;
import com.example.jasper.ccxapp.entities.NewFriend;
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;
import com.example.jasper.ccxapp.interfaces.userBackUserInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    public static void disagreefriend(String userName1, String userName2, final userBackListener userBackListener) {
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
    }

    //同意好友申请，删去申请表中的对应信息，在好友表中添加相应好友信息
    public static void agreenewfriend(final  String userName1,final String userName2,final userBackListener userBackListener) {
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
    }

    //发送好友请求,userName1为发送人，userName2为接受请求人
    public static void sendfriendrequest(String userName1, String userName2, String message, final userBackListener userBackListener) {
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
    }

    //查询当前好友
    public static void searchfriend(String userName1, final userBackListUserInfo userBackListUserInfo) {
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
    public static void searchnewfriend(final String userName1, final String userName2, final userBackUserInfo userBackUserInfo) {
        JMessageClient.getUserInfo(userName2, null, new GetUserInfoCallback(){
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(i == 0){
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
    public static void deletefriend(String userName1, String userName2, final userBackListener userBackListener) {
        com.example.jasper.ccxapp.entities.UserInfo userinfo = null;
        userinfo.removeFromFriendList(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (0 == responseCode) {
                    //移出好友列表成功
                } else {
                    //移出好友列表失败
                }
            }
        });
    }

    //得到该用户被请求为好友的信息
    public static void searchRequestList(String userName, final userBackListListener userBackListListener) {
        BmobQuery<NewFriend> query = new BmobQuery<NewFriend>();
        query.addWhereEqualTo("responseFriend", userName);
        query.setLimit(200);
        query.findObjects(new FindListener<NewFriend>() {
            @Override
            public void done(List<NewFriend> list, BmobException e) {
                ArrayList<String> messages = new ArrayList<String>();
                if(e==null){
                    for(NewFriend friend : list) {
                        messages.add(friend.getRequestFriend());
                        messages.add(friend.getMessage());
                    }
                    userBackListListener.showResult(true, messages);
                }else {
                    Log.e("friend", "查询信息失败" + e.getMessage());
                    messages.add(e.getMessage());
                    userBackListListener.showResult(false, messages);
                }
            }
        });
    }

    //构建Friend类
    private static Friend getFriend(String userName1, String userName2) {
        Friend friend = new Friend();
        friend.setFriend1(userName1);
        friend.setFriend2(userName2);
        return friend;
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
