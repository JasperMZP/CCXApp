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
import com.example.jasper.ccxapp.db.SignUpObj;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by Jasper on 2017/3/21.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mUserId;
    private Button signUpBtn;
    private Button signInBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsernameView = (EditText) findViewById(R.id.login_username);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mUserId = (EditText) findViewById(R.id.login_id);
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
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                finish();
            }
        });
    }

    private void signUp() {
        String username = mUsernameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String userId = mUserId.getText().toString().trim();
        SignUpObj.CreateUser(username,password,userId);
    }


}
