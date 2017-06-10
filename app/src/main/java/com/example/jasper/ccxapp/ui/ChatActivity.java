package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.ChatAdapter;
import com.example.jasper.ccxapp.db.chatDB;
import com.example.jasper.ccxapp.interfaces.GroupInfos;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.GroupInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getChats();
    }

    private void getChats() {
        if(!showProgressDialog(this, "系统提示", "信息加载中，请稍后")){
            return;
        }
        chatDB.getChatroom(new GroupInfos() {
            @Override
            public void showResult(boolean result, ArrayList<GroupInfo> groupInfos) {
                hideProgressDialog();
                if(result){
                    showChatrooms(groupInfos);
                }else{
                    showDialog("查询群聊信息错误！");
                }
            }
        });
    }

    private void showChatrooms(final ArrayList<GroupInfo> groupInfos) {
        ListView lv = (ListView) findViewById(R.id.show_all_chatroom_lv);

        ChatAdapter adapter = new ChatAdapter(ChatActivity.this, groupInfos);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChatActivity.this, ChatDetailActivity.class);
                intent.putExtra("groupId", groupInfos.get(position).getGroupID());
                if(groupInfos.get(position).getGroupOwner().equals(JMessageClient.getMyInfo().getUserName())){
                    intent.putExtra("ifOwn", true);
                }else{
                    intent.putExtra("ifOwn", false);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            startActivity(new Intent(ChatActivity.this, FriendActivity.class));
            this.finish();
        }
        return false;
    }
}
