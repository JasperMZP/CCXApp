package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.util.IOUtil;
import com.example.jasper.ccxapp.view.RecordButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
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
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    private EditText usernameChatToET;
    private Button beginChatBtn;
    private EditText txtMsgET;
    private Button sendTxtMsgBtn;
    private TextView chatContentTV;
    private ImageView chatImageView;
    private Button sendImgMsgBtn;
    private RecordButton sendVoiceMsgBtn;
    private Button showVoiceMsgBtn;
    private Button sendVideoMsgBtn;
    private Button showVideoMsgBtn;
    private VideoView videoView;

    private String usernameChatTo;
    private Conversation mConversation;
    private String txtMsg;
    private String chatContentStr = "";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String voicePath;
    private Uri fileUri;
    private Uri recievedFileuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        beginChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginChat();
            }
        });

        sendTxtMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTxt();
            }
        });

        sendImgMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });

        sendVoiceMsgBtn.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, long intervalTime) {
                Log.i("test", "录音路径" + audioPath);
                voicePath = audioPath;
                sendVoice(voicePath, intervalTime);
            }
        });

        showVoiceMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVoice();
            }
        });

        sendVideoMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(); // create a file Uri to save the video
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            }
        });

        showVideoMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(recievedFileuri);
            }
        });

    }

    private void init() {

        JMessageClient.registerEventReceiver(this);

        usernameChatToET = (EditText) findViewById(R.id.to_chat_username);
        beginChatBtn = (Button) findViewById(R.id.begin_chat);
        txtMsgET = (EditText) findViewById(R.id.text_message);
        sendTxtMsgBtn = (Button) findViewById(R.id.send_message);
        chatContentTV = (TextView) findViewById(R.id.chat_content);
        chatImageView = (ImageView) findViewById(R.id.chat_Image);
        sendImgMsgBtn = (Button) findViewById(R.id.send_Img_btn);

        sendVoiceMsgBtn = (RecordButton) findViewById(R.id.send_voice_btn);
        sendVoiceMsgBtn.setMaxIntervalTime(100);
        sendVoiceMsgBtn.setSavePath("");

        showVoiceMsgBtn = (Button) findViewById(R.id.show_voice_btn);
        sendVideoMsgBtn = (Button) findViewById(R.id.send_video_btn);
        showVideoMsgBtn = (Button) findViewById(R.id.play_video_btn);
        videoView = (VideoView) findViewById(R.id.chat_video_vv);
    }

    private void beginChat() {
        usernameChatTo = usernameChatToET.getText().toString().trim();
        mConversation = Conversation.createSingleConversation(usernameChatTo);
    }

    private void sendTxt() {
        txtMsg = txtMsgET.getText().toString().trim();
        //Message msg = JMessageClient.createSingleTextMessage(usernameChatTo,txtMsg);
        Message message = mConversation.createSendMessage(new TextContent(txtMsg));
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

    private void sendImg(Bitmap bitmap) {
        Message message = mConversation.createSendMessage(new ImageContent(bitmap));
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseDesc) {
                if (responseCode == 0) {
                    //消息发送成功
                    Log.i("test", "图片发送成功");

                } else {
                    //消息发送失败
                    Log.e("test", "图片发送失败");
                }
            }
        });
        JMessageClient.sendMessage(message);
    }

    private void sendVideo(String videopath) {
        try {
            FileContent fileContent = new FileContent(new File(videopath));
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

    private void sendVoice(String voicePath, long intervalTime) {
        try {
            Message message = mConversation.createSendMessage(new VoiceContent(new File(voicePath), (int) intervalTime));
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "语音发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void playVoice() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void playVideo(Uri uri) {
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }

    private void initMediaPlayer(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("test", "初始化mediaplayer完成");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为Bitmap图片格式 ，这是缩略图
            Bitmap bitmap = (Bitmap) bundle.get("data");

            chatImageView.setImageBitmap(bitmap);

            sendImg(bitmap);
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.i("test", "video :" + data.getData().getPath());
            sendVideo(data.getData().getPath());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }


    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        File file = new File(pathString);
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(pathString);
        } else {
            Log.e("test", "该图片不存在");
        }
        return bitmap;
    }

    public static Uri getOutputMediaFileUri() {
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File videoFile = new File(picDir.getPath() + File.separator + "VIDEO_" + timeStamp + ".mp4");

        return Uri.fromFile(videoFile);
    }

    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                Log.i("test", "收到文本消息");
                TextContent textContent = (TextContent) msg.getContent();
                chatContentStr += textContent.getText();
                chatContentTV.setText(chatContentStr);
                break;
            case image:
                Log.i("test", "收到图片消息");
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.downloadOriginImage(msg, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        Log.i("test", "收到的图片下载完成" + i + s);
                        Bitmap img = getDiskBitmap(file.getPath());//图片本地地址
                        chatImageView.setImageBitmap(img);
                    }
                });
                break;
            case voice:
                Log.i("test", "收到语音消息");
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.downloadVoiceFile(msg, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        Log.i("test", "收到的语音下载完成" + i + s + file.getPath());
                        try {
                            IOUtil.copyFile(file, new File("/storage/sdcard/" + file.getName() + ".mp3"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            initMediaPlayer("/storage/sdcard/" + file.getName() + ".mp3");
                        }
                    }
                });
                break;
            case file:
                Log.i("test", "收到视频消息");
                FileContent fileContent = (FileContent) msg.getContent();
                fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int i, String s, File file) {
                        Log.i("test", "收到的文件下载完成" + i + s + file.getPath());
                        try {
                            IOUtil.copyFile(file, new File("/storage/sdcard/" + file.getName() + ".mp4"));
                            recievedFileuri=Uri.parse(file.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

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


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            super.onKeyDown(keyCode, event);
            this.finish();
        }
        return false;
    }
}

