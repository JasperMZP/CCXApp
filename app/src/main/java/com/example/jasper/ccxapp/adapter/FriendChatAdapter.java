package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

public class FriendChatAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Context context;
    private List<UserInfo> userInfos;
    private boolean[] selects;

    public FriendChatAdapter(Context context) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    }

    public FriendChatAdapter(Context context, List<UserInfo> userInfos) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    	this.userInfos = userInfos;
        this.selects = new boolean[userInfos.size()];
        for(int i=0;i<userInfos.size();i++){
            selects[i] = false;
        }
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

    ViewHolder holder;
    @SuppressLint("InflateParams")
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//观察convertView随ListView滚动情况
    	//Log.v("MyListViewBase", "getView " + position + " " + convertView);
    	if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.a_friend_chat, null);
    		holder =new ViewHolder();
    		/*得到各个控件的对象*/
    		holder.a_friend_chat_imageview = (ImageView)convertView.findViewById(R.id.a_friend_chat_iv);
    		holder.a_friend_chat_name = (TextView) convertView.findViewById(R.id.a_friend_chat_name_tv);
			holder.a_friend_chat_checkbox = (CheckBox)convertView.findViewById(R.id.a_friend_chat_cb);
    		convertView.setTag(holder);//绑定ViewHolder对象
    	}
    		/*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        File avatarFile = userInfos.get(position).getAvatarFile();
        holder.a_friend_chat_imageview.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(avatarFile)));;
        holder.a_friend_chat_name.setText(userInfos.get(position).getNickname());
        holder.a_friend_chat_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selects[position] = !selects[position];
            }
        });
		return convertView;
    }

    public final class ViewHolder{
		public TextView a_friend_chat_name;
    	public CheckBox a_friend_chat_checkbox;
        public ImageView a_friend_chat_imageview;
    }

    public ArrayList<String> getUserNameList(){
        ArrayList<String> userNames = new ArrayList<String>();
        for (int i=0;i<userInfos.size();i++){
            if(selects[i]){
                userNames.add(userInfos.get(i).getUserName());
            }
        }
        return userNames;
    }
}
