package com.example.jasper.ccxapp.utils;

import android.util.Log;
import android.view.View;

import com.example.jasper.ccxapp.entities.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Jasper on 2017/3/24.
 */

public class QueryUtil {

    public static User user;
    public static List<User> users;

    public static List<User> getUser() {
        BmobQuery<User> personQuery = new BmobQuery<User>();
        // personQuery.setLimit(50);
        personQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null){
                    //Toast.makeText(MainActivity.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_SHORT).show();
                    Log.d("Query","查询成功：共"+object.size()+"条数据。");
                    /*for (User user : object){
                        //text.append(user.getUsername()+" " + user.getPassword()+"\n");
                    }*/
                    users = object;

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        return users;
    }

    public static User getUser(String objectId){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if(e==null){
                    Log.d("query","查询成功");
                    user = u;
                }else{
                    Log.e("query","查询失败：" + e.getMessage());
                }

            }
        });
        return user;
    }
}
