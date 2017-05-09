package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.ChatMemberAdapter;
import com.example.jasper.ccxapp.db.chatDB;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

public class ChatDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        long groupId = 0;
        groupId = getIntent().getLongExtra("chatroom", groupId);

        getChatMember(groupId);
    }

    private void getChatMember(long groupId) {
        chatDB.getChatMember(groupId, new userBackListUserInfo() {
            @Override
            public void showResult(boolean result, List<UserInfo> message) {
                if(result){
                    showChatMember(message);
                }else{
                    showDialog("查询群聊成员失败");
                }
            }
        });
    }

    private void showChatMember(List<UserInfo> groupMembers) {
        ListView lv = (ListView)findViewById(R.id.show_chat_member);

        ChatMemberAdapter adaptar = new ChatMemberAdapter(ChatDetailActivity.this, groupMembers);
        lv.setAdapter(adaptar);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            startActivity(new Intent(ChatDetailActivity.this, ChatActivity.class));
            this.finish();
        }
        return false;
    }
}
