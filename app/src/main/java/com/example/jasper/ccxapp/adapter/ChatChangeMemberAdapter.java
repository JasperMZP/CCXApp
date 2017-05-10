package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatChangeMemberAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Context context;
    List<String> userNames;
    List<Bitmap> bitmaps;
    private boolean[] selects;

    public ChatChangeMemberAdapter(Context context) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    }

    public ChatChangeMemberAdapter(Context context, List<String> userNames, List<Bitmap> bitmaps) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    	this.userNames = userNames;
        this.bitmaps = bitmaps;
        this.selects = new boolean[userNames.size()];
        for(int i=0;i<userNames.size();i++){
            selects[i] = false;
        }
    }

    @Override
    public int getCount() {
    	return userNames.size();
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
            holder = new ViewHolder();
    		/*得到各个控件的对象*/
            holder.a_friend_chat_imageview = (ImageView) convertView.findViewById(R.id.a_friend_chat_imageView);
            holder.a_friend_chat_name = (TextView) convertView.findViewById(R.id.a_friend_chat_name);
            holder.a_friend_chat_checkbox = (CheckBox) convertView.findViewById(R.id.a_friend_chat_checkbox);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        holder.a_friend_chat_imageview.setImageBitmap(bitmaps.get(position));
        holder.a_friend_chat_name.setText(userNames.get(position));
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

    public List<String> getUserNameList(){
        List<String> userNames2 = new ArrayList<String>();
        for (int i=0;i<userNames.size();i++){
            if(selects[i]){
                userNames2.add(userNames.get(i));
            }
        }
        return userNames2;
    }
}
