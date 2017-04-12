package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.entities.Friend;
import com.example.jasper.ccxapp.entities.NewFriend;
import com.example.jasper.ccxapp.entities.User;
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class friendDB {
    //拒绝好友申请，删除后台中对应好友申请的部分
    public static void disagreefriend(String userName1, String userName2, final userBackListener userBackListener) {
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
                                    Log.d("friend", "删除信息成功");
                                    userBackListener.showResult(true, null);
                                }else{
                                    Log.e("friend", "删除信息失败"+e.getMessage());
                                    userBackListener.showResult(false, "拒绝添加好友失败");
                                }
                            }
                        });
                    }
                }else{
                    Log.e("friend", "删除信息失败"+e.getMessage());
                    userBackListener.showResult(false, "拒绝添加好友失败");
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
                                    addnewfriend(userName1, userName2, userBackListener);
                                    Log.d("friend", "删除信息成功");
                                }else{
                                    userBackListener.showResult(false,e.getMessage());
                                    Log.e("friend", "删除信息失败"+e.getMessage());
                                }
                            }
                        });
                    }
                }else{
                    Log.e("friend", "删除信息失败"+e.getMessage());
                }
            }
        });
    }

    //发送好友请求,userName1为发送人，userName2为接受请求人
    public static void sendfriendrequest(String userName1, String userName2, String message, final userBackListener userBackListener) {
        NewFriend friend = getNewFriend(userName1, userName2, message);
        friend.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.d("friend", "发送好友请求成功");
                    userBackListener.showResult(true, null);
                }else{
                    Log.e("friend", "发送好友请求失败：" + e.getMessage());
                    userBackListener.showResult(false, e.getMessage());
                }
            }
        });
    }

    //查询当前好友，目前最多返回200人
    public static void searchfriend(String userName1, final userBackListListener userBackListListener) {
        BmobQuery<Friend> query = new BmobQuery<Friend>();
        query.addWhereEqualTo("friend1", userName1);
        query.setLimit(200);
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                ArrayList<String> messages = new ArrayList<String>();
                if(e==null){
                    for(Friend friend : list) {
                        messages.add(friend.getFriend2());
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

    //查询新好友
    public static void searchnewfriend(final String userName1, final String userName2, final userBackListListener userBackListListener) {
        BmobQuery<Friend> query = new BmobQuery<Friend>();
        query.addWhereEqualTo("friend1", userName1);
        query.setLimit(200);
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if(e==null){
                    String[] friends = new String[list.size()+1];
                    for(int i=0;i<list.size();i++){
                        friends[i] = list.get(i).getFriend2();
                    }
                    friends[list.size()] = userName1;
                    BmobQuery<User> queryu = new BmobQuery<User>();
                    queryu.addWhereNotContainedIn("username", Arrays.asList(friends));
                    BmobQuery<User> queryu2 = new BmobQuery<User>();
                    queryu2.addWhereEqualTo("username", userName2);
                    List<BmobQuery<User>> querys = new ArrayList<BmobQuery<User>>();
                    querys.add(queryu);
                    querys.add(queryu2);
                    BmobQuery<User> mainQuery = new BmobQuery<User>();
                    mainQuery.and(querys);
                    mainQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if(e==null){
                                ArrayList<String> friends2 = new ArrayList<String>();
                                for(User user : list) {
                                    friends2.add(user.getUsername());
                                }
                                Log.d("friend", "查询信息成功");
                                userBackListListener.showResult(true, friends2);
                            }else {
                                Log.e("friend", "查询信息失败" + e.getMessage());
                                ArrayList<String> messages = new ArrayList<String>();
                                messages.add(e.getMessage());
                                userBackListListener.showResult(false, messages);
                            }
                        }
                    });
                }else {
                    Log.e("friend", "查询信息失败" + e.getMessage());
                    ArrayList<String> messages = new ArrayList<String>();
                    messages.add(e.getMessage());
                    userBackListListener.showResult(false, messages);
                }
            }
        });
    }

    //删除已有好友输入值为好友双方
    public static void deletefriend(String userName1, String userName2, final userBackListener userBackListener) {
        BmobQuery<Friend> eq1 = new BmobQuery<Friend>();
        eq1.addWhereEqualTo("friend1", userName1);
        eq1.addWhereEqualTo("friend2", userName2);
        BmobQuery<Friend> eq2 = new BmobQuery<Friend>();
        eq2.addWhereEqualTo("friend1", userName2);
        eq2.addWhereEqualTo("friend2", userName1);
        List<BmobQuery<Friend>> querys = new ArrayList<BmobQuery<Friend>>();
        querys.add(eq1);
        querys.add(eq2);
        BmobQuery<Friend> mainQuery = new BmobQuery<Friend>();
        mainQuery.or(querys);
        mainQuery.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if(e==null){
                    List<BmobObject> friends = new ArrayList<BmobObject>();
                    for(Friend friend : list) {
                        friends.add(friend);
                    }
                    new BmobBatch().deleteBatch(friends).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if(e==null){
                                Log.d("friend", "删除信息成功");
                                userBackListener.showResult(true, null);
                            }else{
                                Log.e("friend", "删除信息失败2"+e.getMessage());
                                userBackListener.showResult(false, e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.e("friend", "删除信息失败1"+e.getMessage());
                    userBackListener.showResult(false, e.getMessage());
                }
            }
        });
    }

    //根据两个用户名添加新好友
    public static void addnewfriend(String userName1, String userName2, final userBackListener userBackListener) {
        List<BmobObject> friends = new ArrayList<BmobObject>();
        friends.add(getFriend(userName1, userName2));
        friends.add(getFriend(userName2, userName1));
        new BmobBatch().insertBatch(friends).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e==null){
                    Log.d("friend", "添加好友成功");
                    userBackListener.showResult(true, null);
                }else{
                    Log.e("friend", "添加好友失败："+e.getMessage());
                    userBackListener.showResult(false, e.getMessage());
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
