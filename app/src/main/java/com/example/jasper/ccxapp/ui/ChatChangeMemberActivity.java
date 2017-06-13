package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.ChatChangeMemberAdapter;
import com.example.jasper.ccxapp.db.chatDB;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.UserBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.UserBackListener;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;

public class ChatChangeMemberActivity extends AppCompatActivity {

    private Button add_new_member;
    private Button delete_some_member;
    private ChatChangeMemberAdapter adaptar;
    private TextView a_word;
    long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_change_member);

        add_new_member = (Button)findViewById(R.id.add_new_member_btn);
        delete_some_member = (Button)findViewById(R.id.delete_some_member_btn);
        a_word = (TextView)findViewById(R.id.a_word_tv);

        groupId = getIntent().getLongExtra("groupId", groupId);
        String type = getIntent().getStringExtra("type");
        String[] userNames2 = getIntent().getStringArrayExtra("userNames");
        List<String> userNames = new ArrayList<String>();
        for(String userName : userNames2){
            userNames.add(userName);
        }
        if(type.equals("add")){
            add_new_member.setVisibility(View.VISIBLE);
            delete_some_member.setVisibility(View.GONE);
            a_word.setText("其他好友");
            showOtherFriends(userNames);
        }else {
            add_new_member.setVisibility(View.GONE);
            delete_some_member.setVisibility(View.VISIBLE);
            a_word.setText("当前群聊成员");
            List<Bitmap> bitmaps2 = new ArrayList<Bitmap>();
            int i = 0;
            i = getIntent().getIntExtra("bitmaps", i);
            for(int k=0;k<i;k++){
                bitmaps2.add((Bitmap) getIntent().getParcelableExtra("bitmap"+k));
            }
            showChatMember(userNames, bitmaps2);
        }
        setListeners();
    }

    private void showOtherFriends(final List<String> userNames) {
        if(!showProgressDialog(this, "系统提示", "信息加载中，请稍后")){
            return;
        }
        friendDB.searchfriend(new UserBackListUserInfo() {
            @Override

            public void showResult(boolean result, List<UserInfo> message) {
                hideProgressDialog();
                if (result) {
                    List<String> otherUser = new ArrayList<String>();
                    List<Bitmap> otherBimaps = new ArrayList<Bitmap>();
                    for (UserInfo userInfo : message) {
                        boolean flag = true;
                        for (String userName : userNames) {
                            if (userName.equals(userInfo.getUserName())) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            otherUser.add(userInfo.getUserName());
                            otherBimaps.add(BitmapFactory.decodeFile(String.valueOf(userInfo.getAvatarFile())));
                        }
                    }
                    showChatMember(otherUser, otherBimaps);
                } else {
                    showDialog("查询其他好友失败");
                }
            }
        });
    }

    private void setListeners() {
        add_new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChatChangeMemberActivity.this).setTitle("系统提示").setMessage("确定添加这些新成员？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chatDB.addNewMember(groupId, adaptar.getUserNameList(), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
                                        if(result){
                                            Intent intent = new Intent(ChatChangeMemberActivity.this, ChatDetailActivity.class);
                                            intent.putExtra("groupId", groupId);
                                            intent.putExtra("ifOwn", getIntent().getBooleanExtra("ifOwn", false));
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            showDialog("添加新成员失败！");
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        delete_some_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChatChangeMemberActivity.this).setTitle("系统提示").setMessage("确定删除这些成员？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chatDB.deleteSomeMember(groupId, adaptar.getUserNameList(), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
                                        if(result){
                                            Toast.makeText(ChatChangeMemberActivity.this, "删除成员成功！", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ChatChangeMemberActivity.this, ChatDetailActivity.class);
                                            intent.putExtra("groupId", groupId);
                                            intent.putExtra("ifOwn", getIntent().getBooleanExtra("ifOwn", false));
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            showDialog("删除成员失败！");
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    private void showChatMember(List<String> userNames, List<Bitmap> bitmaps) {
        ListView lv = (ListView)findViewById(R.id.show_chat_member_lv);

        adaptar = new ChatChangeMemberAdapter(ChatChangeMemberActivity.this, userNames, bitmaps);
        lv.setAdapter(adaptar);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            Intent intent = new Intent(ChatChangeMemberActivity.this, ChatDetailActivity.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("ifOwn", getIntent().getBooleanExtra("ifOwn", false));
            startActivity(intent);
            this.finish();
        }
        return false;
    }
}
