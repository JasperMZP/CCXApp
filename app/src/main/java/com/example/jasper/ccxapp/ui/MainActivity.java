package com.example.jasper.ccxapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.ShowPhotoAdapter;
import com.example.jasper.ccxapp.entitiy.CommentItemModel;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;

import com.example.jasper.ccxapp.util.UUIDKeyUtil;
import com.example.jasper.ccxapp.util.showMessage;
import com.example.jasper.ccxapp.widget.CustomVideoView;
import com.example.jasper.ccxapp.widget.PinnedHeaderExpandableListView;
import com.example.jasper.ccxapp.widget.RecordButton;
import com.example.jasper.ccxapp.widget.RecyclerItemClickListener;
import com.example.jasper.ccxapp.widget.StickyLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import me.iwf.photopicker.PhotoPreview;

public class MainActivity extends Activity implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener, StickyLayout.OnGiveUpTouchEventListener {
    private final int REQUEST_SEND_MSG_ITEM = 0;

    //View
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private Button addShowBtn;

    //变量
    private ArrayList<ShowItemModel> showList = new ArrayList<ShowItemModel>();
    private ArrayList<List<CommentItemModel>> childCommentList = new ArrayList<List<CommentItemModel>>();
    private MyexpandableListAdapter adapter;
    private Conversation mConversation;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    //收到的图片消息
    private ShowItemModel CheckRecievedShowItem = new ShowItemModel();
    private String imgRecieveFlag = "";
    private String checkShowKey = "";
    private String checkCommKey = "";

    private TextView toFriend;
    private TextView myName;
    private TextView loginout;
    private DrawerLayout drawerLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        adapter = new MyexpandableListAdapter(this);
        expandableListView.setAdapter(adapter);


        // 展开所有group
        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        stickyLayout.setOnGiveUpTouchEventListener(this);

        addShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMsgEditActivity.class);
                startActivityForResult(intent, REQUEST_SEND_MSG_ITEM);
            }
        });
    }

    private void initView() {
        expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
        addShowBtn = (Button) findViewById(R.id.add_show_btn);


        initDrawerLayout();
        drawerLayout.setScrimColor(Color.GRAY);
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        View v1 = (View) findViewById(R.id.left_drawer);
        toFriend = (TextView) v1.findViewById(R.id.tvMyFriend);
        myName = (TextView) v1.findViewById(R.id.myName);
        loginout = (TextView) v1.findViewById(R.id.loginout);
        myName.setText(JMessageClient.getMyInfo().getNickname());
        toFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FriendActivity.class));
            }
        });
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示").setMessage("是否确认退出登录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loginOut();
                            }
                        }).show();
            }
        });
    }

    private void initMediaPlayer(String voicePath) {
        try {
            mediaPlayer.setDataSource(voicePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginOut() {
        try {
            File file = new File(getFilesDir(), "info.properties");
            file.delete();
            File file2 = new File(getFilesDir(), "infoRequest.properties");
            file2.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JMessageClient.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        this.finish();
    }

    private void sendTextMsg(ShowItemModel showItemForSend) {
        TextContent textContent = new TextContent(showItemForSend.getShowText());
        textContent.setStringExtra("showKey", showItemForSend.getMsgKey());
        String groupIds = "";
        for (long groupId : showItemForSend.getGroupBelongToList()) {
            groupIds += groupId + ",";
        }
        textContent.setStringExtra("groupBelongTo", groupIds);
        Message message = mConversation.createSendMessage(textContent);
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //消息发送成功
                    Log.i("test", "文本发送成功");
                } else {
                    //消息发送失败
                    Log.e("test", "文本发送失败");
                }
            }
        });

        JMessageClient.sendMessage(message);
    }


    private void sendImageMsg(ShowItemModel showItemForSend) {
        ArrayList<String> imgPaths = showItemForSend.getShowImagesList();
        String recieveFlag = UUIDKeyUtil.getUUIDKey();
        for (int i = 0; i < imgPaths.size(); i++) {
            try {
                ImageContent imageContent = new ImageContent(new File(imgPaths.get(i)));
                Map extrasMap = new HashMap();
                extrasMap.put("showKey", showItemForSend.getMsgKey());
                if (i == 0 && showItemForSend.getShowText() != null) {
                    extrasMap.put("showText", showItemForSend.getShowText());
                }
                String groupIds = "";
                for (long groupId : showItemForSend.getGroupBelongToList()) {
                    groupIds += groupId + ",";
                }
                Log.i("test", "传递groupIds" + groupIds);
                //imageContent.setStringExtra("groupBelongTo",groupIds);
                extrasMap.put("groupBelongTo", groupIds);
                imageContent.setExtras(extrasMap);
                imageContent.setNumberExtra("imageNum", imgPaths.size());
                imageContent.setNumberExtra("imageCount", i + 1);
                imageContent.setStringExtra("recieveFlag", recieveFlag);
                Message message = mConversation.createSendMessage(imageContent);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test", "发送图片消息" + i + s);
                    }
                });
                JMessageClient.sendMessage(message);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendVideoMsg(ShowItemModel showItemForSend) {
        try {
            FileContent fileContent = new FileContent(new File(showItemForSend.getShowVideo()));
            fileContent.setStringExtra("showKey", showItemForSend.getMsgKey());
            fileContent.setStringExtra("showText", showItemForSend.getShowText());
            String groupIds = "";
            for (long groupId : showItemForSend.getGroupBelongToList()) {
                groupIds += groupId + ",";
            }
            fileContent.setStringExtra("groupBelongTo", groupIds);
            Message message = mConversation.createSendMessage(fileContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "视频发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JMFileSizeExceedException e) {
            e.printStackTrace();
        }
    }

    private void sendVoice(CommentItemModel commentItemForSend) {
        try {
            VoiceContent voiceContent = new VoiceContent(new File(commentItemForSend.getCommentVoice()), commentItemForSend.getCommentLength());
            Map extrasMap = new HashMap();
            extrasMap.put("showKey", commentItemForSend.getMsgKey());
            extrasMap.put("commKey", commentItemForSend.getCommKey());
            extrasMap.put("voiceLength", "" + commentItemForSend.getCommentLength());
            voiceContent.setExtras(extrasMap);
            Message message = mConversation.createSendMessage(voiceContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "发送语音消息" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * InitData
     */
    void initData() {

        showList = new ArrayList<ShowItemModel>();
        ShowItemModel showItem = null;
        for (int i = 0; i < 2; i++) {
            showItem = new ShowItemModel();
            showItem.setMsgKey("" + i);
            showItem.setShowUsername("user" + i);
            showItem.setShowText("show text" + i);
            ArrayList<String> showImgs = new ArrayList<>();
            showImgs.add("/storage/sdcard/Pictures/JPEG_20170421_084405.jpg");
            showImgs.add("/storage/sdcard/Pictures/JPEG_20170421_091106.jpg");
            showImgs.add("/storage/sdcard/Pictures/JPEG_20170421_094403.jpg");
            showItem.setShowImagesList(showImgs);
            showList.add(showItem);
        }

        childCommentList = new ArrayList<List<CommentItemModel>>();


        ArrayList<CommentItemModel> commentItemList = new ArrayList<CommentItemModel>();
        CommentItemModel noneCommentItem1 = new CommentItemModel();
        noneCommentItem1.setMsgKey("-1");
        commentItemList.add(noneCommentItem1);

        childCommentList.add(commentItemList);

        ArrayList<CommentItemModel> commentItemList2 = new ArrayList<CommentItemModel>();
        CommentItemModel noneCommentItem2 = new CommentItemModel();
        noneCommentItem2.setMsgKey("-1");
        commentItemList2.add(noneCommentItem2);

        childCommentList.add(commentItemList2);

    }

    private boolean createConversation(long groupId) {
        mConversation = Conversation.createGroupConversation(groupId);
        GroupInfo groupInfo = (GroupInfo) mConversation.getTargetInfo();
        List<UserInfo> userInfos = groupInfo.getGroupMembers();
        for (UserInfo userInfo : userInfos) {
            Log.i("test", "群成员" + userInfo.getNickname());
            if (userInfo.getUserName().equals(JMessageClient.getMyInfo().getUserName())) {
                return true;
            }
        }
        return false;
    }

    private void sendToGroup(ShowItemModel showItem) {
        //发送的消息体设置
        ShowItemModel showItemForSend = new ShowItemModel();
        showItemForSend.setShowText(showItem.getShowText());
        showItemForSend.setMsgKey(showItem.getMsgKey());
        showItemForSend.setGroupBelongToList(showItem.getGroupBelongToList());

        if (showItem.getShowImagesList() != null) {
            //有图片
            showItemForSend.setShowImagesList(showItem.getShowImagesList());
            //发送带图片的消息
            sendImageMsg(showItemForSend);
        } else if (showItem.getShowVideo() != null) {
            //有视频
            showItemForSend.setShowVideo(showItem.getShowVideo());
            sendVideoMsg(showItemForSend);
        } else if (showItem.getShowImagesList() == null && showItem.getShowVideo() == null) {
            //发送文字信息
            sendTextMsg(showItemForSend);
        }
    }

    /***
     * 数据源
     *
     * @author Administrator
     *
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter{
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return showList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childCommentList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return showList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childCommentList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        private ShowHolder showHolder;

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                showHolder = new ShowHolder();
                convertView = inflater.inflate(R.layout.show_item, null);
                showHolder.showUsernameTv = (TextView) convertView.findViewById(R.id.show_username_tv);
                showHolder.showUserAvatarIv = (ImageView) convertView.findViewById(R.id.show_user_avatar);
                showHolder.showTextTv = (TextView) convertView.findViewById(R.id.show_text_content_tv);
                showHolder.expandedIv = (ImageView) convertView.findViewById(R.id.expanded_img);
                showHolder.showImageRv = (RecyclerView) convertView.findViewById(R.id.show_recycler_view);
                showHolder.showVideoView = (CustomVideoView) convertView.findViewById(R.id.show_video_view);
                showHolder.showVideoPlayBtn = (ImageView) convertView.findViewById(R.id.show_video_play_video_btn);
                convertView.setTag(showHolder);
            } else {
                showHolder = (ShowHolder) convertView.getTag();
            }

            final ShowItemModel showItem = (ShowItemModel) getGroup(groupPosition);
            showHolder.showUsernameTv.setText(showItem.getShowUsername());
            showHolder.showTextTv.setText(showItem.getShowText());

            if (showItem.getShowImagesList() != null) {
                showHolder.showVideoView.setVisibility(View.GONE);
                showHolder.showVideoPlayBtn.setVisibility(View.GONE);
                showHolder.showImageRv.setVisibility(View.VISIBLE);
                ShowPhotoAdapter showPhotoAdapter = new ShowPhotoAdapter(MainActivity.this, showItem.getShowImagesList());
                showHolder.showImageRv.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                showHolder.showImageRv.setAdapter(showPhotoAdapter);
                showHolder.showImageRv.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                PhotoPreview.builder()
                                        .setPhotos(((ShowItemModel) getGroup(groupPosition)).getShowImagesList())
                                        .setCurrentItem(position)
                                        .setShowDeleteButton(false)
                                        .start(MainActivity.this);
                            }
                        }));
                showHolder.expandedIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (expandableListView.isGroupExpanded(groupPosition)) {
                            expandableListView.collapseGroup(groupPosition);
                        } else {
                            expandableListView.expandGroup(groupPosition);
                        }
                    }
                });
            } else if (showItem.getShowVideo() != null) {
                showHolder.showImageRv.setVisibility(View.GONE);
                showHolder.showVideoView.setVisibility(View.VISIBLE);
                showHolder.showVideoPlayBtn.setVisibility(View.VISIBLE);
                showHolder.showVideoView.setVideoHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                showHolder.showVideoView.setVideoWidth(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                showHolder.showVideoView.setVideoPath(showItem.getShowVideo());
                showHolder.showVideoPlayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHolder.showVideoPlayBtn.setVisibility(View.INVISIBLE);
                        showHolder.showVideoView.start();
                    }
                });
                showHolder.showVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        showHolder.showVideoPlayBtn.setVisibility(View.VISIBLE);
                    }
                });
            }else if (showItem.getShowImagesList() == null&&showItem.getShowVideo()==null){
                showHolder.showVideoView.setVisibility(View.GONE);
                showHolder.showVideoPlayBtn.setVisibility(View.GONE);
                showHolder.showImageRv.setVisibility(View.GONE);
            }

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            CommentHolder commentHolder = null;
            if (convertView == null) {
                commentHolder = new CommentHolder();
                convertView = inflater.inflate(R.layout.comment_item, null);
                commentHolder.commentUsernameTv = (TextView) convertView.findViewById(R.id.comment_username_tv);
                commentHolder.playVoiceCommentBtn = (Button) convertView.findViewById(R.id.play_comment_audio_btn);
                commentHolder.sendVoiceCommentBtn = (RecordButton) convertView.findViewById(R.id.send_comment_audio_btn);
                convertView.setTag(commentHolder);
            } else {
                commentHolder = (CommentHolder) convertView.getTag();
            }

            final CommentItemModel commentItem = (CommentItemModel) getChild(groupPosition, childPosition);
            if (commentItem.getMsgKey().equals("-1")) {
                commentHolder.commentUsernameTv.setVisibility(View.GONE);
                commentHolder.playVoiceCommentBtn.setVisibility(View.GONE);
                commentHolder.sendVoiceCommentBtn.setVisibility(View.VISIBLE);
                commentHolder.sendVoiceCommentBtn.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
                    @Override
                    public void onFinishedRecord(String audioPath, long intervalTime) {
                        CommentItemModel commentItemForSend = new CommentItemModel();
                        commentItemForSend.setMsgKey(((ShowItemModel) getGroup(groupPosition)).getMsgKey());
                        commentItemForSend.setCommentUsername(JMessageClient.getMyInfo().getNickname());
                        commentItemForSend.setCommentVoice(audioPath);
                        Log.i("test", "audioPath：" + audioPath);
                        commentItemForSend.setCommentLength((int) (intervalTime / 1000));
                        commentItemForSend.setCommKey(UUIDKeyUtil.getUUIDKey());

                        childCommentList.get(groupPosition).add(getChildrenCount(groupPosition) - 1, commentItemForSend);
                        adapter.notifyDataSetChanged();

                        List<Long> groupBelongtoList = ((ShowItemModel) getGroup(groupPosition)).getGroupBelongToList();
                        if (groupBelongtoList != null) {
                            Log.i("test", "要发送的群id不为空");
                            for (long groupId : groupBelongtoList) {
                                Log.i("test", "要发送的群id：" + groupId);
                                if (createConversation(groupId)) {
                                    sendVoice(commentItemForSend);
                                } else {
                                    Log.i("test", "不属于那个群");
                                }
                            }
                        }
                    }
                });
            } else {
                commentHolder.commentUsernameTv.setVisibility(View.VISIBLE);
                commentHolder.playVoiceCommentBtn.setVisibility(View.VISIBLE);
                commentHolder.sendVoiceCommentBtn.setVisibility(View.GONE);
                commentHolder.commentUsernameTv.setText(commentItem.getCommentUsername());
                commentHolder.playVoiceCommentBtn.setText("" + commentItem.getCommentLength());
                commentHolder.playVoiceCommentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                            initMediaPlayer(commentItem.getCommentVoice());
                        }
                        mediaPlayer.start();
                    }
                });
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Toast.makeText(MainActivity.this,
                childCommentList.get(groupPosition).get(childPosition).getCommentUsername() + "的评论", Toast.LENGTH_SHORT)
                .show();

        return false;
    }

    class ShowHolder {

        TextView showUsernameTv;
        ImageView showUserAvatarIv;
        TextView showTextTv;
        RecyclerView showImageRv;
        CustomVideoView showVideoView;
        ImageView showVideoPlayBtn;
        ImageView expandedIv;
    }

    class CommentHolder {

        TextView commentUsernameTv;
        Button playVoiceCommentBtn;
        RecordButton sendVoiceCommentBtn;

    }

    @Override
    public View getPinnedHeader() {
        View headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.show_item, null);
        headerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        ShowItemModel firstVisibleShowItem = (ShowItemModel) adapter.getGroup(firstVisibleGroupPos);
        TextView showUsernameTv = (TextView) headerView.findViewById(R.id.show_username_tv);
        TextView showTextTv = (TextView) headerView.findViewById(R.id.show_text_content_tv);
        showUsernameTv.setText(firstVisibleShowItem.getShowUsername());
        showTextTv.setText(firstVisibleShowItem.getShowText());
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEND_MSG_ITEM && resultCode == ShowMsgEditActivity.RESULT_SEND_MSG_ITEM) {
            ShowItemModel showItem = (ShowItemModel) data.getSerializableExtra("showItem");
            List<Long> groupBelongtoList = showItem.getGroupBelongToList();

            for (int i = 0; i < groupBelongtoList.size(); i++) {
                Log.i("test", "要发送的群id：" + groupBelongtoList.get(i));
                createConversation(groupBelongtoList.get(i));
                sendToGroup(showItem);
            }

            showItem.setShowUsername(JMessageClient.getMyInfo().getNickname());
            showList.add(0, showItem);

            ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
            CommentItemModel noneComment = new CommentItemModel();
            noneComment.setMsgKey("-1");
            commentItemModels.add(noneComment);
            childCommentList.add(0, commentItemModels);

            adapter.notifyDataSetChanged();
            Log.i("test", "show内容" + showItem.getShowText());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    public void onEventMainThread(MessageEvent event) {

        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                new RecieveTextTask().execute(msg);
                break;
        }


    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case voice:
                //处理语音消息
                new RecieveVoiceTask().execute(msg);
                break;
            case image:
                //处理图片消息
                new RecieveImageTask().execute(msg);
                break;
            case file:
                //处理视频消息
                new RecieveVideoTask().execute(msg);
                break;
        }
    }

    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                showMessage.showNewFriend(MainActivity.this, fromUsername + "请求添加您为好友", "点击查看详细信息");
//                saveRequest(fromUsername, reason);
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }

    /**
     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     * 即可实现离线消息的接收。
     **/
    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
    }


    /**
     * 如果在JMessageClient.init时启用了消息漫游功能，则每当一个会话的漫游消息同步完成时
     * sdk会发送此事件通知上层。
     **/
    public void onEvent(ConversationRefreshEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        //获取事件发生的原因，对于漫游完成触发的事件，此处的reason应该是
        //MSG_ROAMING_COMPLETE
        ConversationRefreshEvent.Reason reason = event.getReason();
        Log.i("test", String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
        Log.i("test", "事件发生的原因 : " + reason);
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
        System.out.println("事件发生的原因 : " + reason);
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class RecieveTextTask extends AsyncTask<Message, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Message... params) {
            Message msg = params[0];
            TextContent textContent = (TextContent) msg.getContent();
            Log.i("test", "收到文本消息" + textContent.getText() + "  showKey " + textContent.getStringExtra("showKey"));
            //跳过群重复消息
            String tShowKey = textContent.getStringExtra("showKey");
            if (tShowKey.equals(checkShowKey)) {
                Log.i("test", "同一个文本消息，跳过");
                return false;
            }
            checkShowKey = tShowKey;

            UserInfo tUserInfo = msg.getFromUser();
            ShowItemModel textShowItem = new ShowItemModel();
            textShowItem.setShowUsername(tUserInfo.getNickname());
            textShowItem.setShowText(textContent.getText());
            textShowItem.setMsgKey(tShowKey);
            String[] groupIds = textContent.getStringExtra("groupBelongTo").split(",");
            List<Long> groupIdBelongTo = new ArrayList<Long>();
            for (int i = 0; i < groupIds.length; i++) {
                groupIdBelongTo.add(Long.parseLong(groupIds[i]));
                Log.i("test", "解析出群:" + groupIds[i]);
            }
            textShowItem.setGroupBelongToList(groupIdBelongTo);
            showList.add(0, textShowItem);

            ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
            CommentItemModel noneComment = new CommentItemModel();
            noneComment.setMsgKey("-1");
            commentItemModels.add(noneComment);
            childCommentList.add(0, commentItemModels);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    class RecieveImageTask extends AsyncTask<Message, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Message... params) {
            Message msg = params[0];
            Log.i("test", "收到图片消息");
            ImageContent imageContent = (ImageContent) msg.getContent();
            UserInfo iUserInfo = msg.getFromUser();
            Map iMsgMap = imageContent.getStringExtras();

            //如果收到的图片key和上一次的一样说明是一个showItem
            if (CheckRecievedShowItem.getMsgKey() != null) {
                if ((iMsgMap.get("showKey")).equals(CheckRecievedShowItem.getMsgKey())) {
                    Log.i("test", "收到多图片消息之一");
                    if (!imageContent.getStringExtra("recieveFlag").equals(imgRecieveFlag)) {
                        Log.i("test", "跳过多余群图片之一");
                        return false;
                    }
                    if (iMsgMap.containsKey("showText")) {
                        CheckRecievedShowItem.setShowText((String) iMsgMap.get("showText"));
                    }
                    ArrayList<String> imgPaths = CheckRecievedShowItem.getShowImagesList();
                    if (imgPaths.contains(imageContent.getLocalThumbnailPath())) {
                        Log.i("test", "跳过重复图片消息");
                        return false;
                    }
                    imgPaths.add(imageContent.getLocalThumbnailPath());

                    Log.i("test", "图片path:" + imageContent.getLocalThumbnailPath());

                    if (imageContent.getNumberExtra("imageNum").equals(imageContent.getNumberExtra("imageCount"))) {

                        Log.i("test", "多图片添加到ListView");

                        String[] iGroupIds = ((String) iMsgMap.get("groupBelongTo")).split(",");
                        Log.i("test", "解析出群");
                        List<Long> iGroupIdBelongTo = new ArrayList<Long>();
                        for (int i = 0; i < iGroupIds.length; i++) {
                            iGroupIdBelongTo.add(Long.parseLong(iGroupIds[i]));
                            Log.i("test", "解析出群:" + iGroupIds[i]);
                        }
                        CheckRecievedShowItem.setGroupBelongToList(iGroupIdBelongTo);

                        showList.add(0, CheckRecievedShowItem);


                        ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
                        CommentItemModel noneComment = new CommentItemModel();
                        noneComment.setMsgKey("-1");
                        commentItemModels.add(noneComment);
                        childCommentList.add(0, commentItemModels);

                    }

                    return true;
                }
            }

            Log.i("test", "收到新图片消息");
            imgRecieveFlag = imageContent.getStringExtra("recieveFlag");
            CheckRecievedShowItem = new ShowItemModel();
            CheckRecievedShowItem.setMsgKey((String) iMsgMap.get("showKey"));
            CheckRecievedShowItem.setShowUsername(iUserInfo.getNickname());
            CheckRecievedShowItem.setShowText("");
            if (iMsgMap.containsKey("showText")) {
                CheckRecievedShowItem.setShowText((String) iMsgMap.get("showText"));
            }
            ArrayList<String> newImagepaths = new ArrayList<String>();
            newImagepaths.add(imageContent.getLocalThumbnailPath());
            CheckRecievedShowItem.setShowImagesList(newImagepaths);
            Log.i("test", "图片path:" + imageContent.getLocalThumbnailPath());

            if (imageContent.getNumberExtra("imageNum").equals(imageContent.getNumberExtra("imageCount"))) {

                Log.i("test", "新图片添加到ListView 只有一张图片");

                String[] iGroupIds = ((String) iMsgMap.get("groupBelongTo")).split(",");
                Log.i("test", "解析出群");
                List<Long> iGroupIdBelongTo = new ArrayList<Long>();
                for (int i = 0; i < iGroupIds.length; i++) {
                    iGroupIdBelongTo.add(Long.parseLong(iGroupIds[i]));
                    Log.i("test", "解析出群:" + iGroupIds[i]);
                }
                CheckRecievedShowItem.setGroupBelongToList(iGroupIdBelongTo);

                showList.add(0, CheckRecievedShowItem);

                ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
                CommentItemModel noneComment = new CommentItemModel();
                noneComment.setMsgKey("-1");
                commentItemModels.add(noneComment);
                childCommentList.add(0, commentItemModels);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    class RecieveVideoTask extends AsyncTask<Message, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Message... params) {
            Message msg = params[0];
            Log.i("test", "收到视频消息");
            FileContent fileContent = (FileContent) msg.getContent();
            //跳过群重复消息
            String fShowKey = fileContent.getStringExtra("showKey");
            if (fShowKey.equals(checkShowKey)) {
                Log.i("test", "同一个视频消息，跳过");
                return false;
            }
            checkShowKey = fShowKey;
            final String[] filePath = new String[1];
            fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                @Override
                public void onComplete(int i, String s, File file) {
                    if (i == 0) {
                        Log.i("test", "收到的视频下载完成" + i + s + file.getPath());
                        filePath[0] = file.getPath();
                    }
                }
            });
            ShowItemModel videoShowItem = new ShowItemModel();
            UserInfo fUserInfo = msg.getFromUser();
            videoShowItem.setShowUsername(fUserInfo.getNickname());
            videoShowItem.setShowText(fileContent.getStringExtra("showText"));
            videoShowItem.setMsgKey(fShowKey);
            videoShowItem.setShowVideo(filePath[0]);
            String[] groupIds = fileContent.getStringExtra("groupBelongTo").split(",");
            List<Long> groupIdBelongTo = new ArrayList<Long>();
            for (int i = 0; i < groupIds.length; i++) {
                groupIdBelongTo.add(Long.parseLong(groupIds[i]));
                Log.i("test", "解析出群:" + groupIds[i]);
            }
            videoShowItem.setGroupBelongToList(groupIdBelongTo);

            showList.add(0, videoShowItem);

            ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
            CommentItemModel noneComment = new CommentItemModel();
            noneComment.setMsgKey("-1");
            commentItemModels.add(noneComment);
            childCommentList.add(0, commentItemModels);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    class RecieveVoiceTask extends AsyncTask<Message, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Message... params) {
            Message msg = params[0];
            Log.i("test", "收到语音消息");
            VoiceContent voiceContent = (VoiceContent) msg.getContent();
            UserInfo vUserInfo = msg.getFromUser();
            Map vMsgMap = voiceContent.getStringExtras();

            //跳过群重复消息
            String vCommKey = (String) vMsgMap.get("commKey");
            if (vCommKey.equals(checkCommKey)) {
                Log.i("test", "同一个语音消息，跳过");
                return false;
            }
            checkCommKey = vCommKey;

            CommentItemModel commentItem = new CommentItemModel();
            commentItem.setMsgKey((String) vMsgMap.get("showKey"));
            commentItem.setCommentUsername(vUserInfo.getNickname());
            commentItem.setCommentLength(Integer.parseInt((String) vMsgMap.get("voiceLength")));

            Log.i("test", "voiceContent.getLocalPath" + voiceContent.getLocalPath());
            commentItem.setCommentVoice(voiceContent.getLocalPath());

            for (int j = 0; j < showList.size(); j++) {
                if (showList.get(j).getMsgKey().equals(commentItem.getMsgKey())) {
                    List<CommentItemModel> commentItemModels = childCommentList.get(j);
                    //防止重复消息
                    boolean flag = true;
                    for (CommentItemModel commentTemp : commentItemModels) {
                        if (commentItem.getCommentVoice().equals(commentTemp.getCommentVoice())) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        commentItemModels.add(commentItemModels.size() - 1, commentItem);
                        childCommentList.remove(j);
                        childCommentList.add(j, commentItemModels);
                        Log.i("test", "添加一条语音消息");
                        return true;

                    }

                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
