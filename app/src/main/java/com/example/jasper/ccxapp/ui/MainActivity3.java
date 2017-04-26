/*
package com.example.jasper.ccxapp.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import com.example.jasper.ccxapp.interfaces.ShowType;
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
import java.util.Map;
import java.util.UUID;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

public class MainActivity3 extends AppCompatActivity implements FileType, ShowType {

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
    private EditText otherMsgET;
    private Button sendOtherMsgBtn;

    private String usernameChatTo;
    private Conversation mConversation;
    private String txtMsg;
    private String chatContentStr = "";
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Uri recievedVideouri;//收到的视频文件uri

    //保存文件的文件夹
    private File videoFolder;
    private File imageFolder;
    private File voiceFolder;

    //保存文件的地址
    private Uri imageSaveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

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
                //sendTxt();
                sendImageTest();
            }
        });

        sendImgMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageSaveUri = getOutputMediaFileUri(IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageSaveUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });

        sendVoiceMsgBtn.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String formerPath, long intervalTime) {
                Log.i("test", "录音路径formerPath " + formerPath);
                String[] pathPartArr = getOutputMediaFileUri(VOICE).getPath().split("\\.");

                Log.i("test", "audio file pathPartArr" + pathPartArr[0] + "  " + pathPartArr[1]);

                pathPartArr[0] += "_" + (int) intervalTime + "_.";
                String newPath = pathPartArr[0] + pathPartArr[1];
                Log.i("test", "录音路径newPath " + newPath);
                File oldFile = new File(formerPath);
                oldFile.renameTo(new File(newPath));
                sendVoice(newPath);
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
                // create a file Uri to save the video
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri(VIDEO));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            }
        });

        showVideoMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recievedVideouri != null) {
                    playVideo(recievedVideouri);
                }
            }
        });

        sendOtherMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOther();
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
        sendVoiceMsgBtn.setMaxIntervalTime(59);

        showVoiceMsgBtn = (Button) findViewById(R.id.show_voice_btn);
        sendVideoMsgBtn = (Button) findViewById(R.id.send_video_btn);
        showVideoMsgBtn = (Button) findViewById(R.id.play_video_btn);
        videoView = (VideoView) findViewById(R.id.chat_video_vv);

        otherMsgET = (EditText) findViewById(R.id.other_msg_et);
        sendOtherMsgBtn = (Button) findViewById(R.id.send_other_btn);

        //创建存放收发文件的目录
        createFileDirs();
    }

    private void beginChat() {
        usernameChatTo = usernameChatToET.getText().toString().trim();
        mConversation = Conversation.createSingleConversation(usernameChatTo);
    }

    private void sendTxt() {
        txtMsg = txtMsgET.getText().toString().trim();
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


    private void sendImg(String imgForSendPath) {
        try {
            FileContent fileContent = new FileContent(new File(imgForSendPath));
            Message message = mConversation.createSendMessage(fileContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "图片发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JMFileSizeExceedException e) {
            e.printStackTrace();
        }
    }

    private void sendImageTest(){
        try {
            Message message = mConversation.createSendMessage(new ImageContent(new File("/storage/sdcard/1.jpg")));
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "图片发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    private void sendVoice(String voicePath) {
        try {
            FileContent fileContent = new FileContent(new File(voicePath));
            Message message = mConversation.createSendMessage(fileContent);
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.i("test", "语音发送" + i + s);
                }
            });
            JMessageClient.sendMessage(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JMFileSizeExceedException e) {
            e.printStackTrace();
        }
    }

    private void sendOther() {
        Map otherMsgMap = new HashMap();
        otherMsgMap.put("show", otherMsgET.getText().toString().trim());
        CustomContent customContent = new CustomContent();
        customContent.setAllValues(otherMsgMap);
        Message message = mConversation.createSendMessage(customContent);
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.i("test", "自定义发送" + i + s);
            }
        });
        JMessageClient.sendMessage(message);
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
            chatImageView.setImageURI(imageSaveUri);
            Log.i("test", "相机返回image :" + imageSaveUri + " " + imageSaveUri.getPath());
            sendImg(imageSaveUri.getPath());
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.i("test", "相机返回video :" + data.getData().getPath());
            sendVideo(data.getData().getPath());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    private Uri getOutputMediaFileUri(String type) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uuidStr = UUID.randomUUID().toString();
        String uuidKey = uuidStr.substring(0, 8) + uuidStr.substring(9, 13) + uuidStr.substring(14, 18) + uuidStr.substring(19, 23) + uuidStr.substring(24);
        switch (type) {
            case IMAGE:
                File imageFile = new File(imageFolder.getPath() + File.separator + SHOW + "_" + IMAGE + "_" + timeStamp + "_" + uuidKey + "_" + ".jpg");
                return Uri.fromFile(imageFile);
            case VIDEO:
                File videoFile = new File(videoFolder.getPath() + File.separator + SHOW + "_" + VIDEO + "_" + timeStamp + "_" + uuidKey + "_" + ".mp4");
                return Uri.fromFile(videoFile);
            case VOICE:
                File voiceFile = new File(voiceFolder.getPath() + File.separator + COMMENT + "_" + VOICE + "_" + timeStamp + "_" + "UUIDKEY" + "_" + ".mp3");
                return Uri.fromFile(voiceFile);
        }

        return null;
    }


    private void createFileDirs() {
        File orginFolder = new File("/storage/sdcard/ccxfile");
        if (!orginFolder.exists()) {
            orginFolder.mkdir();
        }
        videoFolder = new File(orginFolder.getPath() + File.separator + "videofile");
        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
        imageFolder = new File(orginFolder.getPath() + File.separator + "imagefile");
        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        }
        voiceFolder = new File(orginFolder.getPath() + File.separator + "voicefile");
        if (!voiceFolder.exists()) {
            voiceFolder.mkdir();
        }
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
            //包括图片、视频和语音
            case file:
                Log.i("test", "收到文件消息");
                final FileContent fileContent = (FileContent) msg.getContent();
                final String fileName = fileContent.getFileName();
                String[] fileInfo = fileName.split("_");
                switch (fileInfo[1]) {
                    case IMAGE:
                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                try {
                                    Log.i("test", "收到的图片下载完成" + i + s + file.getPath());
                                    File fimage = new File(imageFolder + File.separator + fileName);
                                    IOUtil.copyFile(file, fimage);
                                    chatImageView.setImageURI(Uri.parse(fimage.getPath()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case VIDEO:
                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                Log.i("test", "收到的视频下载完成" + i + s + file.getPath());
                                try {
                                    File fvideo = new File(videoFolder + File.separator + fileName);
                                    IOUtil.copyFile(file, fvideo);
                                    recievedVideouri = Uri.parse(fvideo.getPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case VOICE:
                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                try {
                                    Log.i("test", "收到的语音下载完成" + i + s + file.getPath());
                                    File fvoice = new File(voiceFolder + File.separator + fileName);
                                    IOUtil.copyFile(file, fvoice);
                                    initMediaPlayer(fvoice.getPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                }

            case custom:
                Log.i("test", "收到自定义消息");
                CustomContent customContent = (CustomContent) msg.getContent();
                Map otherMap = customContent.getAllStringValues();

                chatContentStr += (String) otherMap.get("show");
                chatContentTV.setText(chatContentStr);
                break;
            case image:
                Log.i("test", "收到图片消息");
                break;
        }
    }

*
     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     * 即可实现离线消息的接收。
     *

    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Conversation conversation = event.getConversation();
        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
    }


*
     * 如果在JMessageClient.init时启用了消息漫游功能，则每当一个会话的漫游消息同步完成时
     * sdk会发送此事件通知上层。
     *

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

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            super.onKeyDown(keyCode, event);
            this.finish();
        }
        return false;
    }
}

*/
