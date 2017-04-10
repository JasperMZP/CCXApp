package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class NewFriendAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener onClickListener;
    private ArrayList<String> imgPath;
    private ArrayList<String> userName;

    public NewFriendAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public NewFriendAdapter(Context context, View.OnClickListener clickListener, ArrayList<String> imgPath, ArrayList<String> userName) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.imgPath = imgPath;
        this.userName = userName;
        this.onClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return (userName.size()-1)/2;
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
        holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  }
    		/*设置TextView显示的内容，即我们存放在动态数组中的数据*/
//		holder.a_friend_image.setImageBitmap(BitmapFactory.decodeFile(imgPath.get(position)));
        holder.a_friend_name.setText(userName.get(position*2).toString());
        holder.a_friend_request.setText(userName.get(position*2+1).toString());
        holder.btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("系统提示").setMessage("确认添加"+userName.get(position*2).toString()+"为好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendDB.agreenewfriend(userName.get(userName.size()-1), userName.get(position*2).toString(), new userBackListener(){
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
