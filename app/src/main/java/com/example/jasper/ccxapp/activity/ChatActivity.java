package com.example.jasper.ccxapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.jasper.ccxapp.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasper on 2017/3/22.
 */

public class ChatActivity extends AppCompatActivity implements EMMessageListener{

    private TextView chatContent;//已发送消息界面
    private EditText messageText;//待发送的消息
    private Button sendMessage;//发送文字消息按钮
    private Button sendVideoMsg;//发送视频消息按钮
    private Button playBtn;//播放视频
    private VideoView videoView;//视频播放器

    private String toChatUsername;

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Intent intent = null;
    private Uri fileUri = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatContent = (TextView) findViewById(R.id.chat_content);
        messageText = (EditText)findViewById(R.id.text_message);
        sendMessage = (Button)findViewById(R.id.send_message);
        sendVideoMsg = (Button) findViewById(R.id.send_video);
        videoView =(VideoView)findViewById(R.id.video_view);
        playBtn = (Button) findViewById(R.id.play);

        Intent intent = getIntent();
        if (intent!=null){
            toChatUsername = intent.getStringExtra("toChatUsername");
        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });

        sendVideoMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVideo();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               play();
            }
        });

    }

    public void sendVideo(){
        intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//create a intent to record video
        fileUri = getOutputMediaFileUri(); // create a file Uri to save the video

        Log.d("Test1",fileUri.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);


        /*MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(fileUri.toString());
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);// 播放时长单位为毫秒 
        //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(fileUri.toString(), null, Integer.parseInt(duration), toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        // if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(message);*/
    }

    private void play(){
        // Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v");
        //VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(fileUri);
        videoView.start();
        videoView.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +data.getData(),Toast.LENGTH_LONG).show();
                Log.d("Test2",data.getData().toString());
            }
        }
    }

    public static Uri getOutputMediaFileUri() {
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File videoFile = new File(picDir.getPath() + File.separator + "VIDEO_"+ timeStamp + ".mp4");

        return Uri.fromFile(videoFile);
    }






    public void sendText(){
        String content = messageText.getText().toString();
//创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content,
                toChatUsername);
//如果是群聊，设置chattype，默认是单聊
                /*if (chatType == CHATTYPE_GROUP)
                    message.setChatType(EMMessage.ChatType.GroupChat);*/
        message.setChatType(EMMessage.ChatType.Chat);
//发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        chatContent.setText(chatContent.getText()+"\n"+content);

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("easemob","消息发送成功");

            }

            @Override
            public void onError(int i, String s) {
                Log.e("easemob","消息发送失败"+i+s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (final EMMessage message : list){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatContent.setText(chatContent.getText()+"\n"+
                            ((EMTextMessageBody)message.getBody()).getMessage());
                }
            });
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }
}
