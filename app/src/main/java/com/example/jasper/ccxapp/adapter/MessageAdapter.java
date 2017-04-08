package com.example.jasper.ccxapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import com.example.jasper.ccxapp.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<String> user_headimage_list;
    private ArrayList<String> user_name_list;
    private ArrayList<String> user_message_list;
    private ArrayList<Integer> user_message_type;
    private ArrayList<ArrayList<String>> user_commentName_list;
    private ArrayList<ArrayList<String>> audioList;

    public MessageAdapter(Activity activity) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.user_headimage_list = new ArrayList<String>();
        this.user_name_list = new ArrayList<String>();
        this.user_message_list = new ArrayList<String>();
        this.user_message_type = new ArrayList<Integer>();
        this.user_commentName_list = new ArrayList<ArrayList<String>>();
        this.audioList = new ArrayList<ArrayList<String>>();
    }

    @Override
    public int getCount() {
        return user_headimage_list.size();
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
        if (convertView == null) {
            ViewHolder holder;
            convertView = mInflater.inflate(R.layout.a_message, null);
            holder =new ViewHolder();
    		/*得到各个控件的对象*/
            holder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
            holder.userName = (TextView) convertView.findViewById(R.id.username);
            holder.message_text = (TextView) convertView.findViewById(R.id.message_text);
            holder.message_image = (ImageView) convertView.findViewById(R.id.message_image);
            holder.message_video = (VideoView) convertView.findViewById(R.id.message_video);
            holder.all_comment = (ListView) convertView.findViewById(R.id.all_comment);
            holder.send_comment = (Button) convertView.findViewById(R.id.send_comment);
            holder.commentAdapter = new CommentAdapter(activity, user_commentName_list.get(position), audioList.get(position));
            holder.all_comment.setAdapter(holder.commentAdapter);
            convertView.setTag(holder);//绑定ViewHolder对象
        }
        final ViewHolder holder;
        holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象

//            Bitmap bm = BitmapFactory.decodeFile(user_headimage_list.get(position));
//            holder.userImage.setImageBitmap(bm);
        holder.userName.setText(user_name_list.get(position));
        if(user_message_type.get(position) == 1) {
            holder.message_text.setText(user_message_list.get(position));
            holder.message_text.setVisibility(View.VISIBLE);
        }else if(user_message_type.get(position) == 2) {
            Bitmap bm2 = BitmapFactory.decodeFile(user_message_list.get(position));
            holder.message_image.setImageBitmap(bm2);
            holder.message_image.setVisibility(View.VISIBLE);
        }else{
            Uri uri = Uri.parse(user_message_list.get(position));
            holder.message_video.setMediaController(new MediaController(activity));
            holder.message_video.setVideoURI(uri);
//            holder.message_video.start();
            holder.message_video.requestFocus();
            holder.message_video.setVisibility(View.VISIBLE);
        }
        changeListviewHeight(holder.all_comment, holder.commentAdapter);

        //添加新评论
        holder.send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //事例函数变量
                String comment_name = "新评论用户";
                String comment_content = "Environment.getExternalStorageDirectory().getAbsolutePath()" + File.separator + "CCXApp" + File.separator + "voices" + File.separator + "1491300613442.amr";
                //添加新评论，输入为评论用户名，评论内容
                holder.commentAdapter.addNewComment(comment_name,comment_content);
                //修改listview为新高度
                changeListviewHeight(holder.all_comment, holder.commentAdapter);
            }
        });

        return convertView;
    }

    private void changeListviewHeight(ListView all_comment, CommentAdapter commentAdapter) {
        int totalHeight = 0;
        for (int i = 0; i < commentAdapter.getCount(); i++) {
            View listItem = commentAdapter.getView(i, null, all_comment);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = all_comment.getLayoutParams();
        params.height = totalHeight + (all_comment.getDividerHeight() * (all_comment.getCount() - 1));
        all_comment.setLayoutParams(params);
    }

    public void addNewMessage(String imagePath, String userName, String message_content, int type, ArrayList<String> a_user_commentName_list, ArrayList<String> a_audioList) {
        user_headimage_list.add(imagePath);
        user_name_list.add(userName);
        user_message_list.add(message_content);
        user_message_type.add(type);
        user_commentName_list.add(a_user_commentName_list);
        audioList.add(a_audioList);
        notifyDataSetChanged();
    }

    public final class ViewHolder{
        public ImageView userImage;
        public TextView userName;
        public TextView message_text;
        public ImageView message_image;
        public VideoView message_video;
        public ListView all_comment;
        public Button send_comment;
        public CommentAdapter commentAdapter;
    }
}
