package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.PhotoAdapter;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.ShowType;
import com.example.jasper.ccxapp.interfaces.SourceFolder;
import com.example.jasper.ccxapp.util.UUIDKeyUtil;
import com.example.jasper.ccxapp.widget.RecyclerItemClickListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.eventbus.EventBus;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


/**
 * Created by Jasper on 2017/4/20.
 */

public class ShowMsgEditActivity extends AppCompatActivity implements ShowType, SourceFolder {

    public static final int RESULT_SEND_MSG_ITEM = 1;

    private MessageEvent messageEvent;

    private TextView textEditEt;
    private Button msgSendBtn;
    private RecyclerView recyclerView;

    private ShowItemModel showItem;
    private ArrayList<String> photosPath = null;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    private TagFlowLayout mFlowLayout;
    private List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
    private List<String> groupTag = new ArrayList<String>();
    private List<Long> selectedGroupId = new ArrayList<Long>();
    private TagAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg_edit);

        initView();

        getAllGroupList();

        selectPhotoAndPreview();

    }


    private void initView() {
        textEditEt = (EditText) findViewById(R.id.text_edit_et);
        msgSendBtn = (Button) findViewById(R.id.msg_send_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
    }

    private void getAllGroupList() {
        final LayoutInflater mInflater = LayoutInflater.from(ShowMsgEditActivity.this);

        mAdapter = new TagAdapter<String>(groupTag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.show_groups_tag_tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        mFlowLayout.setAdapter(mAdapter);

        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0) {
                    if (!list.isEmpty()) {
                        Log.i("test", "有群");
                        for (long groupId : list) {
                            JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {
                                @Override
                                public void gotResult(int i, String s, GroupInfo groupInfo) {
                                    if (i == 0) {
                                        groupInfoList.add(groupInfo);
                                        groupTag.add(groupInfo.getGroupName());
                                        mAdapter.notifyDataChanged();
                                        Log.i("test", "获取群" + i + s + groupInfo.getGroupName());
                                    }
                                }
                            });
                        }

                    }
                }
            }
        });

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Toast.makeText(ShowMsgEditActivity.this, "选择群：" + selectPosSet.toString(), Toast.LENGTH_SHORT).show();
                selectedGroupId.clear();
                for (int pos : selectPosSet) {
                    selectedGroupId.add(groupInfoList.get(pos).getGroupID());
                }
            }
        });
    }

    private void selectPhotoAndPreview() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        msgSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackShowMsgItemToSend();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(ShowMsgEditActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(ShowMsgEditActivity.this);
                        }
                    }
                }));
    }

    private void BackShowMsgItemToSend() {
        if (selectedGroupId != null && selectedGroupId.size() > 0) {
            showItem = new ShowItemModel();
            showItem.setGroupBelongToList(selectedGroupId);
            showItem.setMsgKey(UUIDKeyUtil.getUUIDKey());
            showItem.setShowText(textEditEt.getText().toString().trim());
            if (photosPath != null) {//有图片
                showItem.setShowImagesList(photosPath);
            }
            Intent intent = new Intent();
            intent.putExtra("showItem", showItem);
            setResult(RESULT_SEND_MSG_ITEM, intent);

        }
        selectedPhotos = null;
        showItem = null;
        photosPath = null;
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            if (data != null) {
                photosPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String photoPath : photosPath) {
                    Log.i("test", photoPath);
                }
            }
            selectedPhotos.clear();

            if (photosPath != null) {

                selectedPhotos.addAll(photosPath);
            }
            photoAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStop() {
        super.onStop();
        if (messageEvent!=null){
            EventBus.getDefault().post(messageEvent);
        }
    }

    public void onEventMainThread(MessageEvent event) {

        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                messageEvent = event;
                break;
        }


    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case voice:
                //处理语音消息
                messageEvent = event;
                break;
            case image:
                //处理图片消息
                messageEvent = event;
                break;
        }
    }

}

