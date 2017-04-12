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


public class FriendActivity extends AppCompatActivity {

	private TextView toNewFriend;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		getFriends();


		toNewFriend = (TextView)findViewById(R.id.to_new_friend);


//		searchNewFriend.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(FriendActivity.this, SearchNewActivity.class));
//			}
//		});
		toNewFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(FriendActivity.this, NewFriendActivity.class));
				finish();
			}
		});
//        to_make_new_chat.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(FriendActivity.this, MakeChatroomActivity.class));
//            }
//        });
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

			//return true;
		}
		if (id == R.id.action_CreateGroup) {
			startActivity(new Intent(FriendActivity.this, MakeChatroomActivity.class));

			//return true;
		}

		return super.onOptionsItemSelected(item);
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