package com.example.jasper.ccxapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jasper.ccxapp.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * Created by Jasper on 2017/3/22.
 */

public class ChatActivity extends AppCompatActivity implements EMMessageListener{

    private TextView chatContent;//已发送消息界面
    private EditText messageText;//待发送的消息
    private Button sendMessage;//发送文字消息按钮
    private Button sendVideoMsg;//发送视频消息按钮

    final String toChatUsername="jasper";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatContent = (TextView) findViewById(R.id.chat_content);
        messageText = (EditText)findViewById(R.id.text_message);
        sendMessage = (Button)findViewById(R.id.send_message);
        sendVideoMsg = (Button) findViewById(R.id.send_video);


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

    }

    public void sendVideo(){
        /*//videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
//如果是群聊，设置chattype，默认是单聊
       // if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);*/
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
