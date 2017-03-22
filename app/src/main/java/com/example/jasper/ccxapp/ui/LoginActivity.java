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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by Jasper on 2017/3/21.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button signUpBtn;
    private Button signInBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameView = (EditText) findViewById(R.id.login_username);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);
        signInBtn = (Button) findViewById(R.id.btn_sign_in);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(mUsernameView.getText().toString().trim(),
                            mPasswordView.getText().toString().trim());
                    Log.e("easemob", "注册成功");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("easemob", "注册失败" + e.getErrorCode() + e.getMessage());
                }
            }
        }).start();
    }

    private void signIn() {
        EMClient.getInstance().login(mUsernameView.getText().toString().trim(),
                mPasswordView.getText().toString().trim(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(int code, String error) {
                        Log.e("easemob", "登录失败"+code+error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("easemob", "登陆中"+progress+status);
                    }
                });
    }
}
