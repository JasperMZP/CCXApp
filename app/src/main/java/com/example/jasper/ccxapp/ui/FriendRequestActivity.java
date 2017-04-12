package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.ArrayList;

/**
 * Created by DPC on 2017/4/7.
 */

public class FriendRequestActivity extends AppCompatActivity{

    private TextView friend_all_request;
    private EditText ownerName;
    private Button btn_agree_friend;
    private Button btn_disagree_friend;
    private Button user2;//这里要获取的是被添加好友的name

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendrequest);

        friend_all_request=(TextView)findViewById(R.id.friend_all_request);
        ownerName = (EditText)findViewById(R.id.login_username);
        btn_agree_friend=(Button)findViewById(R.id.btn_agree_friend);
        btn_disagree_friend=(Button)findViewById(R.id.btn_disagree_friend);

        getRequestList(friend_all_request);

        btn_agree_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreenewfriend();
            }
        });
        btn_disagree_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disagreefriend();
            }
        });

    }

    private void getRequestList(final TextView textView) {
        String userName1 = ownerName.getText().toString().trim();
        if(!checkUserName(userName1)){
            return;
        }
        friendDB.searchRequestList(userName1,
                new userBackListListener(){
                    @Override
                    public void showResult(boolean result, ArrayList<String> message) {
                        if(result){
                            if(message.size()==0){
                                showDialog("暂无好友请求");
                            }else{
                                String data = "";
                                /*
                                for(int i=0;i<message.size();i+=2){
                                    data += message.get(i)+":"+message.get(i+1)+"\n";
                                }*/
                               // showDialog("好友请求查询成功:\n"+data);
                                int i=0;
                                data += message.get(i)+":"+message.get(i+1)+"\n";
                                textView.setText(data+" 请求添加好友");
                            }
                        }else{
                            showDialog("查询好友请求失败");
                        }
                    }
                });
    }

    private void disagreefriend() {
        String userName1 = ownerName.getText().toString().trim();
        String userName2 = user2.getText().toString().trim();
        if(!checkUserName(userName1)){
            return;
        }else if(!checkUserName(userName2)){
            return;
        }

        friendDB.disagreefriend(userName1, userName2,
                new userBackListener(){
                    @Override
                    public void showResult(boolean result, String message) {
                        if(result){
                            showDialog("拒绝好友请求成功");
                        }else{
                            showDialog("拒绝好友请求失败");
                        }
                    }
                });
    }

    private void agreenewfriend() {
        String userName1 = ownerName.getText().toString().trim();
        String userName2 = user2.getText().toString().trim();
        if(!checkUserName(userName1)){
            return;
        }else if(!checkUserName(userName2)){
            return;
        }

        friendDB.agreenewfriend(userName1, userName2,
                new userBackListener(){
                    @Override
                    public void showResult(boolean result, String message) {
                        if(result){
                            showDialog("同意好友请求成功");
                        }else{
                            showDialog("同意好友请求失败");
                        }
                    }
                });
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
}
