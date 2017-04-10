package com.example.jasper.ccxapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by DPC on 2017/4/6.
 */

public class SearchNewActivity extends AppCompatActivity {
    private EditText name_need_search;
    private Button btn_search_new_friend;
    private TextView search_new_friend;
    private ListView all_new_friend;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_newfriend);

        name_need_search=(EditText)findViewById(R.id.name_need_search);
        btn_search_new_friend=(Button)findViewById(R.id.btn_search_new_friend);
        search_new_friend = (TextView)findViewById(R.id.search_new_friend);
        all_new_friend = (ListView)findViewById(R.id.all_new_friend);

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
        }
        friendDB.searchnewfriend(getUserName(), NameNeedSearch, new userBackListListener(){
                    @Override
                    public void showResult(boolean result, ArrayList<String> message) {
                        if(result){
                            if(message.size()==0){
                                search_new_friend.setText("当前没有符合条件的用户");
                                all_new_friend.setVisibility(View.GONE);
                            }else{
                                search_new_friend.setText("搜索结果");
                                showNewFriends(null, message);
                            }
                        }else{
                            showDialog("查询新好友失败");
                        }
                    }
                });
    }

    private void showNewFriends(ArrayList<String> imgPath, final ArrayList<String> message) {
        FriendAdapter adapter = new FriendAdapter(SearchNewActivity.this, null, message);
        all_new_friend.setAdapter(adapter);
        all_new_friend.setVisibility(View.VISIBLE);
		all_new_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(SearchNewActivity.this, SendRequestActivity.class);
				intent.putExtra("newFriendName", message.get(position));
				SearchNewActivity.this.startActivity(intent);
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

    private boolean checkUserName(String userName) {
        if(userName.length() < 5){
            showDialog("用户名不应少于5位字符");
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
            super.onKeyDown(keyCode, event);
            this.finish();
        }
        return false;
    }

}
