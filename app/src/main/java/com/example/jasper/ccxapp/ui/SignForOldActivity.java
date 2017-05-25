package com.example.jasper.ccxapp.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.userDB;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.regex.Pattern;

public class SignForOldActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password1;
    private EditText password2;
    private Button forRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_for_old);

        userName = (EditText) findViewById(R.id.sing_up_userName);
        password1 = (EditText) findViewById(R.id.sing_up_password);
        password2 = (EditText) findViewById(R.id.sing_up_password2);
        forRegister = (Button) findViewById(R.id.sign_up);


        forRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forRegister();
            }
        });
    }

    private void forRegister() {
        final String username = userName.getText().toString().trim();
        final String pwd = password1.getText().toString().trim();
        String pwd2 = password2.getText().toString().trim();
        if(!checkUserName(username)){
            return;
        }else if(!checkpassword(pwd)){
            return;
        }
        if(!pwd.equals(pwd2)){
            showDialog("请输入相同的密码");
            return;
        }
        userDB.addNewUser(username, pwd, new userBackListener(){
            @Override
            public void showResult(boolean result, String message) {
                if(result){
                    finish();
                }else{
                    showDialog("该手机号码已存在!");
                }
            }
        });
    }

    private boolean checkUserName(String userName){
        boolean isPhone = Pattern.compile("^1\\d{10}$").matcher(userName).matches();
        if(!isPhone){
            showDialog("请输入手机号码");
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

    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.finish();
        }
        return false;
    }
}
