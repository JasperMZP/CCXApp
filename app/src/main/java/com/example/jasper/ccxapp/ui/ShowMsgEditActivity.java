package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.adapter.PhotoAdapter;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.FileType;
import com.example.jasper.ccxapp.interfaces.ShowType;
import com.example.jasper.ccxapp.interfaces.SourceFolder;
import com.example.jasper.ccxapp.view.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


/**
 * Created by Jasper on 2017/4/20.
 */

public class ShowMsgEditActivity extends AppCompatActivity implements FileType, ShowType, SourceFolder {

    public static final int RESULT_SEND_MSG_ITEM = 1;

    private TextView textEditEt;
    private Button msgSendBtn;
    private RecyclerView recyclerView;

    private ShowItemModel showItem;
    private ArrayList<String> photosPath = null;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg_edit);

        initView();

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


    private void initView() {
        textEditEt = (EditText) findViewById(R.id.text_edit_et);
        msgSendBtn = (Button) findViewById(R.id.msg_send_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void BackShowMsgItemToSend() {
        showItem = new ShowItemModel();
        showItem.setShowText(textEditEt.getText().toString().trim());
        if (photosPath != null) {//有图片
                showItem.setShowImagesList(photosPath);
        }
        Intent intent = new Intent();
        intent.putExtra("showItem", showItem);
        setResult(RESULT_SEND_MSG_ITEM, intent);

        selectedPhotos=null;
        showItem=null;
        photosPath=null;
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

}

