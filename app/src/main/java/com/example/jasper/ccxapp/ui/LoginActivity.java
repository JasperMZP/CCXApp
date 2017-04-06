package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jasper.ccxapp.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Jasper on 2017/4/6.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText usernameET;
    private EditText passwordET;
    private Button signInBtn;
    private Button signUpBtn;
    private String username;
    private String password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText)findViewById(R.id.login_username);
        passwordET = (EditText)findViewById(R.id.login_password);
        signInBtn = (Button)findViewById(R.id.btn_sign_in);
        signUpBtn = (Button)findViewById(R.id.btn_sign_up);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameET.getText().toString().trim();
                password = passwordET.getText().toString().trim();
                JMessageClient.login(username, password, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test",i+" "+s);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameET.getText().toString().trim();
                password = passwordET.getText().toString().trim();
                JMessageClient.register(username, password, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test",i+" "+s);
                    }
                });
            }
        });
    }
}
