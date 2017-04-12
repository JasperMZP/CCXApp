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
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

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
        chatDB.addnewchatroom(getUserName(), chatName, adapter.getUserNameList(), new userBackListener() {
            @Override
            public void showResult(boolean result, String message) {
                if(result){
                    showDialog2("添加新群聊成功");
                }
            }
        });
    }

    private void getFriendList() {
        String userName = getUserName();

        friendDB.searchfriend(userName, new userBackListListener() {
            @Override
            public void showResult(boolean result, ArrayList<String> message) {
                if(result){
                    showFriends(null, message);
                }else{
                    showDialog("查询好友出错");
                }
            }
        });
    }

    private void showFriends(ArrayList<String> imgPath, ArrayList<String> message) {
        adapter = new FriendChatAdapter(MakeChatroomActivity.this, message);
        showFriendsChat.setAdapter(adapter);
    }

    private boolean checkUserName(String userName) {
        if(userName.length() < 5){
            showDialog("用户名不应少于5位字符");
            return false;
        }
        return true;
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

    public String getUserName(){
        try {
            // 创建File对象
            File file = new File(getFilesDir(), "info.properties");
            // 创建FileIutputStream 对象
            FileInputStream fis = new FileInputStream(file);
            // 创建属性对象
            Properties pro = new Properties();
            // 加载文件
            pro.load(fis);
            // 关闭输入流对象
            fis.close();
            return pro.get("userName").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            this.finish();
        }
        return false;
    }
}
