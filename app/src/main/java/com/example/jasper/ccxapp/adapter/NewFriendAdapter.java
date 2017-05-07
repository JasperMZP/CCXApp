package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.friendDB;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class NewFriendAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener onClickListener;
    private List<UserInfo> userInfos;
    private ArrayList<String> message;

    public NewFriendAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public NewFriendAdapter(Context context, View.OnClickListener clickListener, List<UserInfo> userInfos, ArrayList<String> message) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userInfos = userInfos;
        this.message = message;
        this.onClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //观察convertView随ListView滚动情况
        //Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.a_friend_request, null);
            holder =new ViewHolder();
    		/*得到各个控件的对象*/
            holder.a_friend_image = (ImageView) convertView.findViewById(R.id.a_friend_image);
            holder.a_friend_name = (TextView) convertView.findViewById(R.id.a_friend_name);
            holder.a_friend_request = (TextView)convertView.findViewById(R.id.a_friend_request);
            holder.btn_agree = (Button)convertView.findViewById(R.id.a_new_friend_agree);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        final ViewHolder finalHolder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  }
    		/*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        userInfos.get(position).getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i == 0){
                    finalHolder.a_friend_image.setImageBitmap(bitmap);
                }
            }
        });
        finalHolder.a_friend_name.setText(userInfos.get(position).getNickname()+"("+userInfos.get(position).getUserName()+")");
        finalHolder.a_friend_request.setText(message.get(position));
        finalHolder.btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("系统提示").setMessage("确认添加"+userInfos.get(position).getNickname() +"("+userInfos.get(position).getUserName()+")"+"为好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendDB.agreenewfriend(JMessageClient.getMyInfo().getUserName(), userInfos.get(position).getUserName(), new userBackListener(){
                                    @Override
                                    public void showResult(boolean result, String message) {
                                        onClickListener.onClick(null);
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        return convertView;
    }

    public final class ViewHolder{
        public ImageView a_friend_image;
        public TextView a_friend_name;
        public TextView a_friend_request;
        public Button btn_agree;
    }
}
