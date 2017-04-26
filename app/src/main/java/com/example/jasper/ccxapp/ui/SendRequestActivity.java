package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by DPC on 2017/4/6.
 */

public class SendRequestActivity extends AppCompatActivity{
    private TextView add_friend_name;
    private EditText add_friend_reason;
    private Button btn_send_request_to_add;
    private EditText ownerName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sendrequest);

        add_friend_name = (TextView) findViewById(R.id.add_friend_name);
        add_friend_reason = (EditText) findViewById(R.id.add_friend_reason);
        btn_send_request_to_add = (Button) findViewById(R.id.btn_send_request_to_add);

        add_friend_name.setText(getIntent().getStringExtra("newFriendName"));

        btn_send_request_to_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendfriendrequest();
            }
        });}

    private void sendfriendrequest() {
        String userName1 = getUserName();
        String userName2 = getIntent().getStringExtra("newFriendName");
        String message = add_friend_reason.getText().toString().trim();

        friendDB.sendfriendrequest(userName1, userName2, message,
                new userBackListener(){
                    @Override
                    public void showResult(boolean result, String message) {
                        if(result){
                            showDialog2("发送好友请求成功");
                        }else{
                            showDialog("发送好友请求失败");
                        }
                    }
                });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
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
}
