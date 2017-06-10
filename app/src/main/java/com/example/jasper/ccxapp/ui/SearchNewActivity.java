package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.UserBackUserInfo;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;

/**
 * Created by DPC on 2017/4/6.
 */

public class SearchNewActivity extends AppCompatActivity {
    private EditText name_need_search;
    private ImageView btn_search_new_friend;
    private TextView search_new_friend;
    private LinearLayout all_new_friend;
    private View a_line;
    private ImageView a_friend_image;
    private TextView a_friend_name;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_newfriend);

        name_need_search=(EditText)findViewById(R.id.name_need_search_et);
        btn_search_new_friend=(ImageView)findViewById(R.id.search_new_friend_iv);
        search_new_friend = (TextView)findViewById(R.id.search_new_friend_tv);
        all_new_friend = (LinearLayout)findViewById(R.id.all_new_friend);
        a_line = findViewById(R.id.a_line);
        a_friend_image = (ImageView)findViewById(R.id.a_friend_image_iv);
        a_friend_name = (TextView)findViewById(R.id.a_friend_name_tv);

        btn_search_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchfriend();
            }
        });
    }

    private void searchfriend() {
        String NameNeedSearch = name_need_search.getText().toString().trim();
        if(NameNeedSearch.equals("")){
            showDialog("请输入关键词！");
            return;
        }else{
            if(NameNeedSearch.equals(JMessageClient.getMyInfo().getUserName())){
                showDialog("不能添加自身为好友！");
            }
        }
        if(!showProgressDialog(this, "系统提示", "信息加载中，请稍后")){
            return;
        }
        friendDB.searchnewfriend(NameNeedSearch, new UserBackUserInfo() {
            @Override
            public void showResult(boolean result, String s, UserInfo message) {
                hideProgressDialog();
                if (result) {
                    search_new_friend.setText("搜索结果");
                    showNewFriends(message);
                    a_line.setVisibility(View.VISIBLE);
                } else {
                    search_new_friend.setText(s);
                    all_new_friend.setVisibility(View.GONE);
                    a_line.setVisibility(View.GONE);
                    showDialog(s);
                }
            }
        });
    }

    private void showNewFriends(final UserInfo message) {
        final Bitmap[] bitmap2 = {null};
        message.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                a_friend_image.setImageBitmap(bitmap);
                bitmap2[0] = bitmap;
            }
        });
        if(message.getNickname().equals(message.getUserName())){
            a_friend_name.setText(message.getNickname());
        }else {
            a_friend_name.setText(message.getNickname() + "(" + message.getUserName() + ")");
        }
        all_new_friend.setVisibility(View.VISIBLE);
		all_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchNewActivity.this, SendRequestActivity.class);
                intent.putExtra("newFriendName", a_friend_name.getText().toString());
                intent.putExtra("userImage", bitmap2[0]);
                intent.putExtra("newFriendName2", message.getUserName());
                SearchNewActivity.this.startActivity(intent);
            }
		});
    }


    private void showDialog(String message) {
        new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
                .setPositiveButton("确定", null).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            Intent intent = new Intent(SearchNewActivity.this, FriendActivity.class);
            startActivity(intent);
            this.finish();
        }
        return false;
    }

}
