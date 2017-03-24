package com.example.jasper.ccxapp.activity;

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

/**
 * Created by Jasper on 2017/3/24.
 */

public class SignInActivity extends AppCompatActivity {
    private EditText mPasswordView;
    private EditText mUserId;
    private Button signUpBtn;
    private Button signInBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mUserId = (EditText) findViewById(R.id.login_id);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);
        signInBtn = (Button) findViewById(R.id.btn_sign_in);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        EMClient.getInstance().login(mUserId.getText().toString().trim(),
                mPasswordView.getText().toString().trim(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
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
