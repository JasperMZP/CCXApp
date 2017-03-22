package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.entities.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private EditText otherUsername;
    private Button beginChat;

    //private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //text = (TextView) findViewById(R.id.text);

        otherUsername = (EditText) findViewById(R.id.other_username);
        beginChat = (Button) findViewById(R.id.begin_chat);


        beginChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChatActivity.class));
            }
        });
    }

   /* public void CreateUser(View view) {
        final User user = new User();
        user.setUsername("Jasper");
        user.setPassword("123456");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void GetUser(View view) {
        BmobQuery<User> personQuery = new BmobQuery<User>();
        personQuery.setLimit(50);
        personQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null){
                    Toast.makeText(MainActivity.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_SHORT).show();
                    for (User user : object){
                        text.append(user.getUsername()+" " + user.getPassword()+"\n");
                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }*/
}

