package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.FriendAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListUserInfo;

import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;


public class FriendActivity extends AppCompatActivity {

	private TextView toNewFriend;
	private TextView toMyChat;
	private ImageView img_toNewFriend;
	private ImageView img_toGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		getFriends();

		img_toNewFriend= (ImageView)findViewById(R.id.image_to_new_friend);
		img_toGroup= (ImageView)findViewById(R.id.to_group);
		toMyChat = (TextView)findViewById(R.id.to_my_chat);
		toNewFriend = (TextView)findViewById(R.id.to_new_friend);
		toMyChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, ChatActivity.class));
				finish();
			}
		});
		toNewFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, NewFriendActivity.class));
				finish();
			}
		});
		img_toNewFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, NewFriendActivity.class));
				finish();
			}
		});
		img_toGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, ChatActivity.class));
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_actions, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_addFriend) {
			startActivity(new Intent(FriendActivity.this, SearchNewActivity.class));
			finish();
			//return true;
		}
		if (id == R.id.action_CreateGroup) {
			startActivity(new Intent(FriendActivity.this, MakeChatroomActivity.class));

			//return true;
		}

		return super.onOptionsItemSelected(item);
	}
	private void getFriends() {
		friendDB.searchfriend(new userBackListUserInfo() {
			@Override
			public void showResult(boolean result, List<UserInfo> message) {
				if(result){
					showFriends(message);
				}else{
					showDialog("查询好友出错");
				}
			}
		});
	}

	private void showFriends(List<UserInfo> message) {
		ListView lv = (ListView) findViewById(R.id.all_friend);

		FriendAdapter adapter = new FriendAdapter(FriendActivity.this, message);
		lv.setAdapter(adapter);
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