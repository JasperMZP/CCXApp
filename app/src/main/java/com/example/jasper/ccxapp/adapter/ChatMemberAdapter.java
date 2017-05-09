package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

public class ChatMemberAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Context context;
    private List<UserInfo> userInfos;

    public ChatMemberAdapter(Context context) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    }

    public ChatMemberAdapter(Context context, List<UserInfo> userInfos) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    	this.userInfos = userInfos;
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
    public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.a_friend, null);
    		holder =new ViewHolder();
    		/*得到各个控件的对象*/
    		holder.a_friend_image = (ImageView) convertView.findViewById(R.id.a_friend_image);
    		holder.a_friend_name = (TextView) convertView.findViewById(R.id.a_friend_name);
    		convertView.setTag(holder);//绑定ViewHolder对象
    	}
		holder = (ViewHolder)convertView.getTag();
		File avatarFile = userInfos.get(position).getAvatarFile();
		holder.a_friend_image.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(avatarFile)));

		String userName2 = userInfos.get(position).getUserName();
		try {
			String nickName2 = userInfos.get(position).getNickname();
			holder.a_friend_name.setText(nickName2 + "(" + userName2 + ")");
		} catch (Exception e){
			holder.a_friend_name.setText(userName2);
		}
		return convertView;
    }

    public final class ViewHolder{
		public ImageView a_friend_image;
    	public TextView a_friend_name;
    }
}
