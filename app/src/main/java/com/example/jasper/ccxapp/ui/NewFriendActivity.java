package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.NewFriendAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.UserBackListListener;
import com.example.jasper.ccxapp.interfaces.UserBackListener;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

import static com.example.jasper.ccxapp.util.ShowProcessUtil.hideProgressDialog;
import static com.example.jasper.ccxapp.util.ShowProcessUtil.showProgressDialog;


public class NewFriendActivity extends AppCompatActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);

		getFriendRequest();

	}

	private void getFriendRequest() {
		String userName = JMessageClient.getMyInfo().getUserName();
		if(!showProgressDialog(this, "系统提示", "信息加载中，请稍后")){
			return;
		}
		friendDB.searchRequestList(userName, new UserBackListListener() {
			@Override
			public void showResult(boolean result, ArrayList<String> message, List<cn.jpush.im.android.api.model.UserInfo> userInfos) {
				hideProgressDialog();
				if(result){
					showFriendRequest(userInfos, message);
				}else{
					showDialog("查询好友请求出错");
				}
			}
		});
	}

	private void showFriendRequest(final List<UserInfo> userInfos, final ArrayList<String> message) {
		ListView lv = (ListView) findViewById(R.id.all_friend_request_lv);

		NewFriendAdapter adapter = new NewFriendAdapter(NewFriendActivity.this, NewFriendActivity.this, userInfos, message);
		lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(NewFriendActivity.this).setTitle("系统提示").setMessage("确认删除此信息？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showProgressDialog(NewFriendActivity.this, "系统提示", "信息加载中，请稍后");
								friendDB.disagreefriend(JMessageClient.getMyInfo().getUserName(), userInfos.get(position).getUserName(), new UserBackListener(){
									@Override
									public void showResult(boolean result, String message) {
										hideProgressDialog();
                                        if(result){
											getFriendRequest();
										}else{
                                            showDialog("删除信息失败");
                                        }
									}
								});
							}
						}).setNegativeButton("取消", null).show();
				return true;
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
			startActivity(new Intent(NewFriendActivity.this, FriendActivity.class));
			this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		getFriendRequest();
	}
}