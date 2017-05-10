package com.example.jasper.ccxapp.db;

import android.util.Log;

import com.example.jasper.ccxapp.interfaces.GroupInfos;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class chatDB {
    //添加新聊天室
    public static void addnewchatroom(String userName, String chatroom, final ArrayList<String> otherusers, final userBackListener userBackListener) {
        JMessageClient.createGroup(chatroom, "咱一家人的描述", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, final long l) {
                Log.i("test", "创建群聊" + "a" + i + " b" + s + " c" + l);
                final long groupId = l;

                JMessageClient.addGroupMembers(groupId, otherusers, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test", "添加群成员" + i + s);
                        if(i == 0) {
                            userBackListener.showResult(true, "");
                        }else {
                            userBackListener.showResult(false, s);
                        }
                    }
                });
            }
        });
    }

    public static void getChatroom(final GroupInfos backgroupInfos){
        final ArrayList<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
        final boolean[] flag = {true};
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, final List<Long> list) {
                if(i == 0){
                    for(Long achatroom : list){
                        JMessageClient.getGroupInfo(achatroom, new GetGroupInfoCallback() {
                            @Override
                            public void gotResult(int i, String s, GroupInfo groupInfo) {
                                if(i == 0) {
                                    groupInfos.add(groupInfo);
                                }else{
                                    if(flag[0]){
                                        flag[0] = false;
                                        backgroupInfos.showResult(false, null);
                                        return;
                                    }
                                }
                                if(groupInfos.size() == list.size()){
                                    backgroupInfos.showResult(true, groupInfos);
                                }
                            }
                        });
                    }
                }else {
                    backgroupInfos.showResult(false, null);
                }
            }
        });
    }

    public static void getChatMember(long groupID, final userBackListUserInfo userBackListUserInfo){
        JMessageClient.getGroupMembers(groupID, new GetGroupMembersCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                if(i == 0){
                    userBackListUserInfo.showResult(true, list);
                }else {
                    userBackListUserInfo.showResult(false, null);
                }
            }
        });
    }

    public static void addNewMember(long groupID, List<String> userNames, final userBackListener userBackListener){
        JMessageClient.addGroupMembers(groupID, userNames, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    userBackListener.showResult(true, null);
                }else {
                    userBackListener.showResult(false, s);
                }
            }
        });
    }

    public static void deleteSomeMember(long groupID, List<String> userNames, final userBackListener userBackListener){
        JMessageClient.removeGroupMembers(groupID, userNames, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    userBackListener.showResult(true, null);
                }else {
                    userBackListener.showResult(false, s);
                }
            }
        });
    }

    public static void quitChat(long groupID, final userBackListener userBackListener){
        JMessageClient.exitGroup(groupID, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    userBackListener.showResult(true, null);
                }else {
                    userBackListener.showResult(false, s);
                }
            }
        });
    }
}
