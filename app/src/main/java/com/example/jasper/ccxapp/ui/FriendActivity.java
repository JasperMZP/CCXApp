package com.example.jasper.ccxapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.FriendAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;


public class FriendActivity extends Activity {

	private TextView toNewFriend;
	private TextView searchNewFriend;
    private TextView to_make_new_chat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		getFriends();

    	searchNewFriend = (TextView) findViewById(R.id.search_new_friend);
		toNewFriend = (TextView)findViewById(R.id.to_new_friend);
        to_make_new_chat = (TextView)findViewById(R.id.make_new_chat);

		searchNewFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, SearchNewActivity.class));
			}
		});
		toNewFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, NewFriendActivity.class));
				finish();
			}
		});
        to_make_new_chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FriendActivity.this, MakeChatroomActivity.class));
            }
        });
	}

	private void getFriends() {
		String userName = getUserName();

		friendDB.searchfriend(userName, new userBackListListener() {
			@Override
			public void showResult(boolean result, ArrayList<String> message) {
				if(result){
					showFriends(null, message);
				}else{
					showDialog("查询好友出错");
				}
			}
		});
	}

	private void showFriends(ArrayList<String> imgPath, ArrayList<String> message) {
		ListView lv = (ListView) findViewById(R.id.all_friend);

		FriendAdapter adapter = new FriendAdapter(FriendActivity.this, imgPath, message);
		lv.setAdapter(adapter);
//		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent = new Intent();
//				intent.setClass(FriendActivity.this, AddNewActivity.class);
//				intent.putExtra("newFriendName", newFriends.getString(arg2));
//				FriendActivity.this.startActivity(intent);
//				FriendActivity.this.finish();
//			}
//		});
	}

	public String getUserName(){
		try {
            // 创建File对象
            File file = new File(getFilesDir(), "info.properties");
            // 创建FileIutputStream 对象
            FileInputStream fis = new FileInputStream(file);
            // 创建属性对象
            Properties pro = new Properties();
            // 加载文件
            pro.load(fis);
            // 关闭输入流对象
            fis.close();
            return pro.get("userName").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
	}

	private void showDialog(String message) {
		new AlertDialog.Builder(this).setTitle("系统提示").setMessage(message)
				.setPositiveButton("确定", null).show();
	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			super.onKeyDown(keyCode, event);
			this.finish();
		}
		return false;
	}
}