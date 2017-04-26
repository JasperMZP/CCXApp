package com.example.jasper.ccxapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.example.jasper.ccxapp.widget.PinnedHeaderExpandableListView;
import com.example.jasper.ccxapp.widget.RecordButton;
import com.example.jasper.ccxapp.widget.RecyclerItemClickListener;
import com.example.jasper.ccxapp.widget.StickyLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import me.iwf.photopicker.PhotoPreview;

public class MainActivity extends Activity implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener, StickyLayout.OnGiveUpTouchEventListener {

    //View
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;
    private Button addShowBtn;

    //变量
    private ArrayList<ShowItemModel> showList = new ArrayList<ShowItemModel>();

    private ArrayList<List<CommentItemModel>> childCommentList = new ArrayList<List<CommentItemModel>>();


    private MyexpandableListAdapter adapter;

    private Conversation mConversation;
    private final int REQUEST_SEND_MSG_ITEM = 0;

    //收到的图片消息
    private ShowItemModel recieveImageShowItem = new ShowItemModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        createGroup();
        initData();

        adapter = new MyexpandableListAdapter(this);
        expandableListView.setAdapter(adapter);

        //showPhotoAdapter = new ShowPhotoAdapter(this, showPhotos);


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

    private void createGroup() {
        Log.i("test", "当前用户" + JMessageClient.getMyInfo().getUserName());
        if (JMessageClient.getMyInfo().getUserName().equals("test1")) {
            mConversation = Conversation.createSingleConversation("test2");
        } else if (JMessageClient.getMyInfo().getUserName().equals("test2")) {
            mConversation = Conversation.createSingleConversation("test1");
        }
        /*JMessageClient.createGroup("咱一家人2333", "咱一家人的描述", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, final long l) {
                Log.i("test", "创建群聊" + "a"+i + " b" + s + " c" + l);
                final long groupId = l;

                List<String> groupMembers = new ArrayList<String>();
                groupMembers.add("test1");
                groupMembers.add("test3");
                JMessageClient.addGroupMembers(groupId, groupMembers, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("test", "添加群成员" + i + s);


                        JMessageClient.getGroupMembers(groupId, new GetGroupMembersCallback() {
                            @Override
                            public void gotResult(int i, String s, List<UserInfo> list) {
                                Log.i("test", "获取群成员" + i + s);
                                for (UserInfo userInfo : list) {
                                    Log.i("test", "群成员" + userInfo.getUserName());
                                }
                            }
                        });
                    }
                });
            }
        });*/


        /*JMessageClient.getGroupMembers(22868325, new GetGroupMembersCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                Log.i("test", "获取群成员" + i + s);
                for (UserInfo userInfo : list) {
                    Log.i("test", "群成员" + userInfo.getUserName());
                }
            }
        });*/
        /*mConversation = Conversation.createGroupConversation(22894523);
        GroupInfo groupInfo = (GroupInfo) mConversation.getTargetInfo();
        List<UserInfo> userInfos = groupInfo.getGroupMembers();
        for (UserInfo userInfo : userInfos) {
            Log.i("test", "群成员" + userInfo.getUserName());
        }*/

    }

    private void initView() {
        expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
        addShowBtn = (Button) findViewById(R.id.add_show_btn);
    }

    private void sendTextMsg(ShowItemModel showItemForSend) {
        TextContent textContent = new TextContent(showItemForSend.getShowText());
        textContent.setStringExtra("showKey", showItemForSend.getMsgKey());
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
        for (int i = 0; i < imgPaths.size(); i++) {
            try {
                ImageContent imageContent = new ImageContent(new File(imgPaths.get(i)));
                Map extrasMap = new HashMap();
                extrasMap.put("showKey", showItemForSend.getMsgKey());
                if (i == 0 && showItemForSend.getShowText() != null) {
                    extrasMap.put("showText", showItemForSend.getShowText());
                }
                imageContent.setExtras(extrasMap);
                imageContent.setNumberExtra("imageNum", imgPaths.size());
                imageContent.setNumberExtra("imageCount", i + 1);
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

    private void sendVoice(CommentItemModel commentItemForSend) {
        try {
            VoiceContent voiceContent = new VoiceContent(new File(commentItemForSend.getCommentVoice()), commentItemForSend.getCommentLength());
            Map extrasMap = new HashMap();
            extrasMap.put("showKey", commentItemForSend.getMsgKey());
            extrasMap.put("voiceLength", ""+commentItemForSend.getCommentLength());
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

    /***
     * 数据源
     *
     * @author Administrator
     *
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
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

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            ShowHolder showHolder = null;
            if (convertView == null) {
                showHolder = new ShowHolder();
                convertView = inflater.inflate(R.layout.show_item, null);
                showHolder.showUsernameTv = (TextView) convertView.findViewById(R.id.show_username_tv);
                showHolder.showUserAvatarIv = (ImageView) convertView.findViewById(R.id.show_user_avatar);
                showHolder.showTextTv = (TextView) convertView.findViewById(R.id.show_text_content_tv);
                showHolder.expandedIv = (ImageView) convertView.findViewById(R.id.expanded_img);
                showHolder.showImageRv = (RecyclerView) convertView.findViewById(R.id.show_recycler_view);
                convertView.setTag(showHolder);
            } else {
                showHolder = (ShowHolder) convertView.getTag();
            }


            final ShowItemModel showItem = (ShowItemModel) getGroup(groupPosition);
            showHolder.showUsernameTv.setText(showItem.getShowUsername());
            showHolder.showTextTv.setText(showItem.getShowText());
            if (showItem.getShowImagesList() != null) {
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
            } else {
                showHolder.showImageRv.setAdapter(null);
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


            CommentItemModel commentItem = (CommentItemModel) getChild(groupPosition, childPosition);
            if (commentItem.getMsgKey().equals("-1")){
                commentHolder.commentUsernameTv.setVisibility(View.GONE);
                commentHolder.playVoiceCommentBtn.setVisibility(View.GONE);
                commentHolder.sendVoiceCommentBtn.setVisibility(View.VISIBLE);
                commentHolder.sendVoiceCommentBtn.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
                    @Override
                    public void onFinishedRecord(String audioPath, long intervalTime) {
                        CommentItemModel commentItemForSend = new CommentItemModel();
                        commentItemForSend.setMsgKey(((ShowItemModel) getGroup(groupPosition)).getMsgKey());
                        commentItemForSend.setCommentUsername(JMessageClient.getMyInfo().getUserName());
                        commentItemForSend.setCommentVoice(audioPath);
                        Log.i("test","audioPath："+audioPath);
                        commentItemForSend.setCommentLength((int) (intervalTime / 1000));

                        childCommentList.get(groupPosition).add(getChildrenCount(groupPosition)-1,commentItemForSend);
                        sendVoice(commentItemForSend);

                        adapter.notifyDataSetChanged();


                    }
                });
            }else {
                commentHolder.commentUsernameTv.setVisibility(View.VISIBLE);
                commentHolder.playVoiceCommentBtn.setVisibility(View.VISIBLE);
                commentHolder.sendVoiceCommentBtn.setVisibility(View.GONE);
                commentHolder.commentUsernameTv.setText(commentItem.getCommentUsername());
                commentHolder.playVoiceCommentBtn.setText("" + commentItem.getCommentLength());
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
        VideoView showVideoVv;
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
            showItem.setShowUsername(JMessageClient.getMyInfo().getUserName());
            showList.add(0, showItem);

            //test
            ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
            CommentItemModel noneComment = new CommentItemModel();
            noneComment.setMsgKey("-1");
            commentItemModels.add(noneComment);
            childCommentList.add(commentItemModels);

            adapter.notifyDataSetChanged();
            Log.i("test", "show内容" + showItem.getShowText());

            //发送的消息体设置
            ShowItemModel showItemForSend = new ShowItemModel();
            showItemForSend.setShowText(showItem.getShowText());
            showItemForSend.setMsgKey(showItem.getMsgKey());

            //sendCustomMsg(showItemForSend);


            //有图片
            if (showItem.getShowImagesList() != null) {
                showItemForSend.setShowImagesList(showItem.getShowImagesList());
                //发送带图片的消息
                sendImageMsg(showItemForSend);
            } else {//发送文字信息
                sendTextMsg(showItemForSend);
            }
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
                TextContent textContent = (TextContent) msg.getContent();
                Log.i("test", "收到文本消息" + textContent.getText() + "  showKey " + textContent.getStringExtra("showKey"));
                UserInfo tUserInfo = msg.getFromUser();
                ShowItemModel textShowItem = new ShowItemModel();
                textShowItem.setShowUsername(tUserInfo.getUserName());
                textShowItem.setShowText(textContent.getText());
                textShowItem.setMsgKey(textContent.getStringExtra("showKey"));
                showList.add(0, textShowItem);

                //test
                ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
                CommentItemModel noneComment=new CommentItemModel();
                noneComment.setMsgKey("-1");
                commentItemModels.add(noneComment);
                childCommentList.add(0,commentItemModels);

                adapter.notifyDataSetChanged();
                break;
            case file:
                Log.i("test", "收到文件消息");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;
        }


    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case voice:
                //处理语音消息
                Log.i("test", "收到语音消息");
                VoiceContent voiceContent = (VoiceContent)msg.getContent();
                UserInfo vUserInfo = msg.getFromUser();
                Map vMsgMap = voiceContent.getStringExtras();
                CommentItemModel commentItem = new CommentItemModel();
                commentItem.setMsgKey((String) vMsgMap.get("showKey"));
                commentItem.setCommentUsername(vUserInfo.getUserName());
                commentItem.setCommentLength(Integer.parseInt((String) vMsgMap.get("voiceLength")));

                Log.i("test","voiceContent.getLocalPath"+voiceContent.getLocalPath());
                commentItem.setCommentVoice(voiceContent.getLocalPath());

                for (int j=0;j<showList.size();j++){
                    if (showList.get(j).getMsgKey().equals(commentItem.getMsgKey())){
                        List<CommentItemModel> commentItemModels = childCommentList.get(j);
                        //防止重复消息
                        boolean flag = true;
                        for (CommentItemModel commentTemp:commentItemModels){
                            if (commentItem.getCommentVoice().equals(commentTemp.getCommentVoice())){
                                flag=false;
                            }
                        }
                        if (flag){
                            commentItemModels.add(commentItemModels.size()-1,commentItem);
                            childCommentList.remove(j);
                            childCommentList.add(j,commentItemModels);
                            Log.i("test", "添加一条语音消息");
                            adapter.notifyDataSetChanged();
                        }

                    }
                }

                break;
            case image:
                //处理图片消息
                Log.i("test", "收到图片消息");
                ImageContent imageContent = (ImageContent) msg.getContent();
                UserInfo iUserInfo = msg.getFromUser();
                Map iMsgMap = imageContent.getStringExtras();

                //如果收到的图片key和上一次的一样说明是一个showItem
                if (recieveImageShowItem.getMsgKey() != null) {
                    if ((iMsgMap.get("showKey")).equals(recieveImageShowItem.getMsgKey())) {
                        Log.i("test", "收到多图片消息之一");
                        if (iMsgMap.containsKey("showText")) {
                            recieveImageShowItem.setShowText((String) iMsgMap.get("showText"));
                        }
                        ArrayList<String> imgPaths = recieveImageShowItem.getShowImagesList();
                        if (imgPaths.contains(imageContent.getLocalThumbnailPath())) {
                            Log.i("test", "跳过重复图片消息");
                            break;
                        }
                        imgPaths.add(imageContent.getLocalThumbnailPath());

                        Log.i("test", "图片path:" + imageContent.getLocalThumbnailPath());

                        if (imageContent.getNumberExtra("imageNum").equals(imageContent.getNumberExtra("imageCount"))) {

                            Log.i("test", "多图片添加到ListView");

                            showList.add(0, recieveImageShowItem);


                            ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
                            CommentItemModel noneComment=new CommentItemModel();
                            noneComment.setMsgKey("-1");
                            commentItemModels.add(noneComment);
                            childCommentList.add(0,commentItemModels);

                            adapter.notifyDataSetChanged();
                        }

                        break;
                    }
                }

                Log.i("test", "收到新图片消息");
                recieveImageShowItem = new ShowItemModel();
                recieveImageShowItem.setMsgKey((String) iMsgMap.get("showKey"));
                recieveImageShowItem.setShowUsername(iUserInfo.getUserName());
                recieveImageShowItem.setShowText("");
                if (iMsgMap.containsKey("showText")) {
                    recieveImageShowItem.setShowText((String) iMsgMap.get("showText"));
                }
                ArrayList<String> newImagepaths = new ArrayList<String>();
                newImagepaths.add(imageContent.getLocalThumbnailPath());
                recieveImageShowItem.setShowImagesList(newImagepaths);
                Log.i("test", "图片path:" + imageContent.getLocalThumbnailPath());

                if (imageContent.getNumberExtra("imageNum").equals(imageContent.getNumberExtra("imageCount"))) {

                    Log.i("test", "新图片添加到ListView 只有一张图片");

                    showList.add(0, recieveImageShowItem);

                    ArrayList<CommentItemModel> commentItemModels = new ArrayList<CommentItemModel>();
                    CommentItemModel noneComment=new CommentItemModel();
                    noneComment.setMsgKey("-1");
                    commentItemModels.add(noneComment);
                    childCommentList.add(0,commentItemModels);

                    adapter.notifyDataSetChanged();
                }

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
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
        System.out.println("事件发生的原因 : " + reason);
    }

}
