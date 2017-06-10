package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.FriendAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.fragment.FriendFragment;
import com.example.jasper.ccxapp.interfaces.UserBackListUserInfo;
import com.example.jasper.ccxapp.interfaces.UserBackListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;


public class FriendActivity extends AppCompatActivity {

	private TextView toNewFriend;
	private TextView toMyChat;
	private ImageView img_toNewFriend;
	private ImageView img_toGroup;
	FragmentManager fragmentManager = getSupportFragmentManager();
	FriendFragment friendFragment = (FriendFragment) FriendFragment.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		//getFriends();
		showDefaultFragment();
		img_toNewFriend= (ImageView)findViewById(R.id.image_to_new_friend_iv);
		img_toGroup= (ImageView)findViewById(R.id.to_group_iv);
		toMyChat = (TextView)findViewById(R.id.to_my_chat_tv);
		toNewFriend = (TextView)findViewById(R.id.to_new_friend_tv);
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
	private void showDefaultFragment() {

		switchFragment(R.id.fragment_container_FriendActivity,
				friendFragment, FriendFragment.TAG);

	}
	private void switchFragment(int id, Fragment fragment, String tag) {
		fragmentManager.beginTransaction().
				replace(id, fragment, tag).commit();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.friend_actions, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_addFriend) {
			startActivity(new Intent(FriendActivity.this, SearchNewActivity.class));
			finish();
		}
		if (id == R.id.action_CreateGroup) {
			startActivity(new Intent(FriendActivity.this, MakeChatroomActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

    public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			super.onKeyDown(keyCode, event);
			this.finish();
		}
		return false;
	}

}