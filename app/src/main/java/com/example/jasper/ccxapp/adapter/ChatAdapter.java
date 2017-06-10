package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

import java.util.ArrayList;

import cn.jpush.im.android.api.model.GroupInfo;

public class ChatAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Context context;
    private ArrayList<GroupInfo> groupInfos;

    public ChatAdapter(Context context) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    }

    public ChatAdapter(Context context, ArrayList<GroupInfo> groupInfos) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    	this.groupInfos = groupInfos;
    }

    @Override
    public int getCount() {
    	return groupInfos.size();
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
    		convertView = mInflater.inflate(R.layout.a_chatroom, null);
    		holder =new ViewHolder();
    		/*得到各个控件的对象*/
    		holder.chatroom_name = (TextView) convertView.findViewById(R.id.chatroom_name_tv);
    		convertView.setTag(holder);//绑定ViewHolder对象
    	}
		holder = (ViewHolder)convertView.getTag();
		String groupName = groupInfos.get(position).getGroupName();
		holder.chatroom_name.setText(groupName);
		return convertView;
    }

    public final class ViewHolder{
    	public TextView chatroom_name;
    }
}
