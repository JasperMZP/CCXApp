package com.example.jasper.ccxapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView headImage;
    private TextView userName;
    private TextView nickName;
    private TextView sex;
    private TextView birthday;
    private TextView address;
    private TextView explain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        headImage = (ImageView)findViewById(R.id.add_message_image_civ);
        userName = (TextView)findViewById(R.id.message_userName_tv);
        nickName = (TextView)findViewById(R.id.message_nickname_tv);
        sex = (TextView)findViewById(R.id.message_sex_tv);
        birthday = (TextView)findViewById(R.id.show_birthday_tv);
        address = (TextView)findViewById(R.id.message_address_tv);
        explain = (TextView)findViewById(R.id.message_explain_tv);

        headImage.setImageBitmap((Bitmap) getIntent().getParcelableExtra("headImage"));
        userName.setText(getIntent().getStringExtra("userName"));
        nickName.setText(getIntent().getStringExtra("nickName"));
        sex.setText(getIntent().getStringExtra("sex"));
        birthday.setText(getIntent().getStringExtra("birthday"));
        address.setText(getIntent().getStringExtra("address"));
        explain.setText(getIntent().getStringExtra("explain"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.finish();
        }
        return false;
    }
}
