package com.example.jasper.ccxapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.MessageAdapter;
import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ListView all_message;
    private Button addNewMessage;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private MessageAdapter messageAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        all_message = (ListView)findViewById(R.id.all_messages);
        addNewMessage = (Button)findViewById(R.id.add_new_message);

        addNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDatas();
            }
        });

        messageAdapter = new MessageAdapter(this);
        all_message.setAdapter(messageAdapter);

        initDrawerLayout();
        drawerLayout.setScrimColor(Color.GRAY);
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //v4控件 actionbar上的抽屉开关，可以实现一些开关的动态效果
//        toggle = new ActionBarDrawerToggle(this, drawerLayout,
//                R.drawable.star_change, R.string.drawer_open
//                , R.string.drawer_close) {
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);//抽屉关闭后
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);//抽屉打开后
//            }
//        };
//        drawerLayout.setDrawerListener(toggle);

    }

//    //上面说到方便使用者随处调用就是这个方法，只需调用这个方法绑定id即可随处控制抽屉的拉出
//    private void toggleRightSliding(){//该方法控制右侧边栏的显示和隐藏
//        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
//            drawerLayout.closeDrawer(GravityCompat.END);//关闭抽屉
//        }else{
//            drawerLayout.openDrawer(GravityCompat.END);//打开抽屉
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_personal:
//                toggleRightSliding();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


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
        //添加文本消息
//        messageAdapter.addNewMessage("","姓名","文本消息",1,a_user_comment_name_list,a_user_comment_comment);
        //添加图片消息
//        messageAdapter.addNewMessage("","姓名",Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                + "DCIM" + File.separator + "Camera" + File.separator + "IMG_20170405_183626.jpg"
//                ,2,a_user_comment_name_list,a_user_comment_comment);
        //添加视频信息
        messageAdapter.addNewMessage("","姓名",Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + "DCIM" + File.separator + "Camera" + File.separator + "VID_20170405_191102.mp4"
                ,3,a_user_comment_name_list,a_user_comment_comment);
    }
}
