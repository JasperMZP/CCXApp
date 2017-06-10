package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		getFriends();

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
	private void getFriends() {
		if(!showProgressDialog(this, "系统提示", "信息加载中，请稍后")){
			return;
		}
		friendDB.searchfriend(new UserBackListUserInfo() {
			@Override
			public void showResult(boolean result, List<UserInfo> message) {
				hideProgressDialog();
                if(result){
					showFriends(message);
				}else{
					showDialog("查询好友出错");
				}
		}
		});
	}

	private void showFriends(List<UserInfo> message) {
		final List<UserInfo> userInfosOld = new ArrayList<>();
		final List<UserInfo> userInfosYoung = new ArrayList<>();
		for(UserInfo userInfo:message){
            try {
                if (userInfo.getRegion().equals("old")) {
                    userInfosOld.add(userInfo);
                } else {
                    userInfosYoung.add(userInfo);
                }
            }catch (Exception e){
                userInfosYoung.add(userInfo);
            }
		}
        ListView lv = (ListView) findViewById(R.id.all_friend_lv);
        FriendAdapter adapter = new FriendAdapter(FriendActivity.this, userInfosOld);
        lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(FriendActivity.this).setTitle("系统提示").setMessage("确认删除该老人？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								friendDB.deletefriend(userInfosOld.get(position), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
										if(result){
                                            showDialog("删除成功！");
                                            getFriends();
                                        }else{
                                            showDialog("删除失败！");
                                        }
                                    }
                                });
							}
						}).setNegativeButton("取消", null).show();
				return true;
			}
		});
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = getIntent(userInfosOld.get(position));
				startActivity(i);
			}
		});

        ListView lv2 = (ListView) findViewById(R.id.all_young_friend_lv);
        FriendAdapter adapter2 = new FriendAdapter(FriendActivity.this, userInfosYoung);
        lv2.setAdapter(adapter2);
        lv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(FriendActivity.this).setTitle("系统提示").setMessage("确认删除该好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendDB.deletefriend(userInfosYoung.get(position), new UserBackListener() {
                                    @Override
                                    public void showResult(boolean result, String message) {
                                        if(result){
                                            showDialog("删除好友成功！");
                                            getFriends();
                                        }else{
                                            showDialog("删除好友失败！");
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        });
		lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = getIntent(userInfosYoung.get(position));
				startActivity(i);
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
			this.finish();
		}
		return false;
	}

	private Intent getIntent(UserInfo userDetail){
		Intent i = new Intent(FriendActivity.this, UserDetailActivity.class);
		File avatarFile = userDetail.getAvatarFile();
		i.putExtra("headImage", BitmapFactory.decodeFile(String.valueOf(avatarFile)));
		i.putExtra("userName", userDetail.getUserName());
		i.putExtra("nickName", userDetail.getNickname());
		UserInfo.Gender sex2 = userDetail.getGender();
		String sex;
		if(sex2 == UserInfo.Gender.female){
			sex = "女";
		}else {
			sex = "男";
		}
		i.putExtra("sex", sex);
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		i.putExtra("birthday", date.format(userDetail.getBirthday()).toString());
		i.putExtra("address", userDetail.getAddress());
		i.putExtra("explain", userDetail.getSignature());
		return i;
	}
}