package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.jasper.ccxapp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<String> user_commentName_list;
    private ArrayList<String> audioList;

    public CommentAdapter(Activity activity) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public CommentAdapter(Activity activity, ArrayList<String> userNameList, ArrayList<String> audioList2) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.user_commentName_list = new ArrayList<String>();
        this.audioList = new ArrayList<String>();
        for(int i=0;i<userNameList.size();i++){
            user_commentName_list.add(userNameList.get(i));
            audioList.add(audioList2.get(i));
        }
    }

    @Override
    public int getCount() {
        return user_commentName_list.size();
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.a_comment, null);
            holder =new ViewHolder();
            holder.comment_user = (TextView) convertView.findViewById(R.id.comment_user);
            holder.show_audio = (Button) convertView.findViewById(R.id.comment_audio_show);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        holder.comment_user.setText(user_commentName_list.get(position));
        holder.show_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudio(audioList.get(position));
            }
        });
        return convertView;
    }

    private void startAudio(String filePath) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewComment(String comment_name, String comment_content) {
        user_commentName_list.add(comment_name);
        audioList.add(comment_content);
        notifyDataSetChanged();
    }

    public final class ViewHolder{
        public TextView comment_user;
        public Button show_audio;
    }
}
