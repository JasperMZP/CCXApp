package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;

import java.util.ArrayList;

public class FriendChatAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Context context;
    private ArrayList<String> userName;
    private boolean[] selects;

    public FriendChatAdapter(Context context) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    }

    public FriendChatAdapter(Context context, ArrayList<String> userName) {
    	this.mInflater = LayoutInflater.from(context);
    	this.context = context;
    	this.userName = userName;
        this.selects = new boolean[userName.size()];
        for(int i=0;i<userName.size();i++){
            selects[i] = false;
        }
    }

    @Override
    public int getCount() {
    	return userName.size();
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
    		convertView = mInflater.inflate(R.layout.a_friend_chat, null);
    		holder =new ViewHolder();
    		/*得到各个控件的对象*/
    		holder.a_friend_chat_name = (TextView) convertView.findViewById(R.id.a_friend_chat_name);
			holder.a_friend_chat_checkbox = (CheckBox)convertView.findViewById(R.id.a_friend_chat_checkbox);
    		convertView.setTag(holder);//绑定ViewHolder对象
    	}
		holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  }
    		/*设置TextView显示的内容，即我们存放在动态数组中的数据*/
//		holder.a_friend_image.setImageBitmap(BitmapFactory.decodeFile(imgPath.get(position)));
		holder.a_friend_chat_name.setText(userName.get(position).toString());
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
    }

    public ArrayList<String> getUserNameList(){
        ArrayList<String> userNames = new ArrayList<String>();
        for (int i=0;i<userName.size();i++){
            if(selects[i]){
                userNames.add(userName.get(i));
            }
        }
        return userNames;
    }
}
