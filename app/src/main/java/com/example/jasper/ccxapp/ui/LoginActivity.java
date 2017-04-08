package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.userDB;
import com.example.jasper.ccxapp.interfaces.userBackListener;

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
        setContentView(R.layout.activity_sign_in);

        usernameET = (EditText)findViewById(R.id.login_username);
        passwordET = (EditText)findViewById(R.id.login_password);
        signInBtn = (Button)findViewById(R.id.btn_sign_in);
        signUpBtn = (Button)findViewById(R.id.btn_sign_up);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forLogin();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegister();
            }
        });
    }

    private void toRegister() {
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        this.finish();
    }

    private void forLogin() {
        username = usernameET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        if(!checkUserName(username)){
            return;
        }else if(!checkpassword(password)){
            return;
        }
        userDB.forUserLogin(username, password, new userBackListener() {
            @Override
            public void showResult(boolean result, String message) {
                if(result){
//                    JMessageClient.login(username, password, new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            Log.i("test",i+" "+s);
                            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                            finish();
//                        }
//                    });
                }else{
                    showDialog("用户名或密码错误");
                }
            }
        });
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkUserName(String userName){
        if(userName.length() < 5){
            showDialog("用户名不应少于5位字符");
            return false;
        }
        return true;
    }

    private boolean checkpassword(String password){
        if(password.length() < 6){
            showDialog("密码不应少于6位字符");
            return false;
        }
        return true;
    }

}
