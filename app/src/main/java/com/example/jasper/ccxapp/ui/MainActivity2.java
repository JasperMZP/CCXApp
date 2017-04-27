/*
package com.example.jasper.ccxapp.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.MessageAdapter;
import com.example.jasper.ccxapp.util.showMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;

public class MainActivity2 extends AppCompatActivity {

    private ListView all_message;
    private TextView toFriend;
    private TextView myName;
    private TextView loginout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private MessageAdapter messageAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        setContentView(R.layout.activity_main2);

        all_message = (ListView)findViewById(R.id.all_messages);

        messageAdapter = new MessageAdapter(this);
        all_message.setAdapter(messageAdapter);

        initDrawerLayout();
        drawerLayout.setScrimColor(Color.GRAY);
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        View v1 = (View)findViewById(R.id.left_drawer);
        toFriend = (TextView) v1.findViewById(R.id.tvMyFriend);
        myName = (TextView)v1.findViewById(R.id.myName);
        loginout = (TextView)v1.findViewById(R.id.loginout);
        myName.setText(getUserName());
        toFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, FriendActivity.class));
            }
        });
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity2.this).setTitle("系统提示").setMessage("是否确认退出登录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loginOut();
                            }
                        }).show();
            }
        });
    }

    private void loginOut() {
        try {
            File file = new File(getFilesDir(), "info.properties");
            file.delete();
            File file2 = new File(getFilesDir(), "infoRequest.properties");
            file2.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JMessageClient.logout();
        startActivity(new Intent(MainActivity2.this, LoginActivity.class));
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
           addDatas();

            //return true;
        }
        if (id == R.id.action_send) {
            startActivity(new Intent(MainActivity2.this,MainActivity.class));

            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //添加一条新信息，需要在addNewMessage中依次输入图片路径，用户名，信息内容，信息格式，评论中用户列表，评论中音频列表
    //信息格式1为文字，2为图片路径，3为视频路径
    //用户列表和音频列表为数组
    private void addDatas() {
        //获得读取本地数据权限
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        ArrayList<String> a_user_comment_name_list = new ArrayList<String>();
        ArrayList<String> a_user_comment_comment = new ArrayList<String>();
        a_user_comment_name_list.add("回复1");
        a_user_comment_comment.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "CCXApp" + File.separator + "voices" + File.separator + "1491389314764.amr");
        if(num%2 == 0) {
            num++;
            //添加文本消息
            messageAdapter.addNewMessage("", "姓名", "文本消息", 1, a_user_comment_name_list, a_user_comment_comment);
        }else {
            num++;
            //添加图片消息
            messageAdapter.addNewMessage("", "姓名", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                            + "DCIM" + File.separator + "Camera" + File.separator + "IMG_20170405_183626.jpg"
                    , 2, a_user_comment_name_list, a_user_comment_comment);
        }
//        //添加视频信息
//        messageAdapter.addNewMessage("","姓名",Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                        + "DCIM" + File.separator + "Camera" + File.separator + "VID_20170405_191102.mp4"
//                ,3,a_user_comment_name_list,a_user_comment_comment);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                showMessage.showNewFriend(MainActivity2.this, fromUsername+"请求添加您为好友", "点击查看详细信息");
//                saveRequest(fromUsername, reason);
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }

    private void saveRequest(String fromUsername, String reason) {
        try {
            File file = new File(getFilesDir(), "infoRequest.properties");
            FileInputStream fis = new FileInputStream(file);
            Properties pro = new Properties();
            pro.load(fis);
            fis.close();
            file.delete();
            FileOutputStream fos = new FileOutputStream(file);
            Properties pro2 = new Properties();
            pro2.setProperty("userName", pro.getProperty("userName"));
            pro2.setProperty("requestName", pro.getProperty("requestName").toString()+fromUsername+"|");
            pro2.setProperty("reason", pro.getProperty("reason").toString()+reason+"|");
            pro2.store(fos, "infoRequest.properties");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
