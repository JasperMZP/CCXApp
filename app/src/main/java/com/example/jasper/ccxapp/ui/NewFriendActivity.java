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
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.NewFriendAdapter;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListListener;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;


public class NewFriendActivity extends AppCompatActivity implements OnClickListener {

	private TextView toNewFriend;
	private TextView searchNewFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);

		getFriendRequest();

	}

	private void getFriendRequest() {
		String userName = getUserName();
//		try {
////			File file = new File(getFilesDir(), "infoRequest.properties");
////			FileInputStream fis = new FileInputStream(file);
////			Properties pro = new Properties();
////			pro.load(fis);
////			fis.close();
////			ArrayList<String> message = new ArrayList<String>();
////            String[] names = pro.getProperty("requestName").toString().split("|");
////            String[] reasons = pro.getProperty("reason").toString().split("|");
////            for(int i=0;i<names.length;i++){
////                message.add(names[i]);
////                message.add(reasons[i]);
////            }
////            showFriendRequest(null, message);
//		} catch (Exception e) {
//			e.printStackTrace();
//            showDialog("查询好友请求出错");
//		}
		friendDB.searchRequestList(userName, new userBackListListener() {
			@Override
			public void showResult(boolean result, ArrayList<String> message) {
				if(result){
					showFriendRequest(null, message);
				}else{
					showDialog("查询好友请求出错");
				}
			}
		});
	}

	private void showFriendRequest(ArrayList<String> imgPath, final ArrayList<String> message) {
		ListView lv = (ListView) findViewById(R.id.all_friend_request);
		message.add(getUserName());

		NewFriendAdapter adapter = new NewFriendAdapter(NewFriendActivity.this, NewFriendActivity.this, imgPath, message);
		lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(NewFriendActivity.this).setTitle("系统提示").setMessage("确认删除此信息？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								friendDB.disagreefriend(message.get(message.size()-1), message.get(position*2).toString(), new userBackListener(){
									@Override
									public void showResult(boolean result, String message) {
										getFriendRequest();
                                        if(!result){
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