package com.example.jasper.ccxapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jasper.ccxapp.R;

public class MainActivity extends AppCompatActivity {

    private EditText toChatUsername;
    private Button beginChat;

    //private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //text = (TextView) findViewById(R.id.text);

        toChatUsername = (EditText) findViewById(R.id.to_chat_username);
        beginChat = (Button) findViewById(R.id.begin_chat);


        beginChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("toChatUsername",toChatUsername.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

}

