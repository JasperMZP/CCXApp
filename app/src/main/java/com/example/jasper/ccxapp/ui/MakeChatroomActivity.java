package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.FriendChatAdapter;
import com.example.jasper.ccxapp.db.chatDB;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

public class MakeChatroomActivity extends AppCompatActivity {

    private ListView showFriendsChat;
    private Button addNewChat;
    private FriendChatAdapter adapter;
    private EditText chatroomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_chatroom);

        showFriendsChat = (ListView)findViewById(R.id.all_friend_for_chat);
        addNewChat = (Button)findViewById(R.id.add_new_chatroom);
        chatroomName = (EditText)findViewById(R.id.add_new_chatroom_name);

        getFriendList();

        addNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChatroom();
            }
        });
    }

    private void addNewChatroom() {
        String chatName = chatroomName.getText().toString().trim();
        if(chatName.equals("")){
            showDialog("请输入群聊名称！");
            return;
        }
        if(adapter.getUserNameList().size() == 0){
            showDialog("请选择加入群聊的人！");
            return;
        }
        chatDB.addnewchatroom(null, chatName, adapter.getUserNameList(), new userBackListener() {
            @Override
            public void showResult(boolean result, String message) {
                if(result){
                    showDialog2("添加新群聊成功");
                }else{
                    showDialog("添加新群聊失败");
                }
            }
        });
    }

    private void getFriendList() {
        friendDB.searchfriend(new userBackListUserInfo() {
            @Override
            public void showResult(boolean result, List<UserInfo> message) {
                if(result){
                    showFriends(message);
                }else{
                    showDialog("查询好友出错");
                }
            }
        });
    }

    private void showFriends(List<UserInfo> message) {
        adapter = new FriendChatAdapter(MakeChatroomActivity.this, message);
        showFriendsChat.setAdapter(adapter);
    }


    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    private void showDialog2(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            this.finish();
        }
        return false;
    }
}
