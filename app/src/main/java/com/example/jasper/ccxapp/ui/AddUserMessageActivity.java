package com.example.jasper.ccxapp.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jasper.ccxapp.R;
import com.example.jasper.ccxapp.db.userDB;
import com.example.jasper.ccxapp.interfaces.userBackListener;

import java.io.FileNotFoundException;

public class AddUserMessageActivity extends AppCompatActivity {

    private ImageView message_image;
    private Button btn_image;
    private EditText userName;
    private EditText nickName;
    private RadioGroup message_sex;
    private EditText message_birthday;
    private EditText message_address;
    private EditText message_explain;
    private String image_path;
    private Button add_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_message);

        Toast.makeText(this, "注册完成，请填写详细信息", Toast.LENGTH_SHORT).show();

        message_image = (ImageView)findViewById(R.id.add_message_image);
        btn_image = (Button)findViewById(R.id.add_message_image_btn);
        userName = (EditText)findViewById(R.id.message_userName);
        nickName = (EditText)findViewById(R.id.message_nickname);
        message_sex = (RadioGroup)findViewById(R.id.message_sex);
        message_birthday = (EditText)findViewById(R.id.message_birthday);
        message_address = (EditText)findViewById(R.id.message_address);
        message_explain = (EditText)findViewById(R.id.message_explain);
        add_message = (Button)findViewById(R.id.add_message_image_btn);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
        
        add_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserMessage();
            }
        });
    }

    private void saveUserMessage() {
        String username = userName.getText().toString().trim();
        String nickname = nickName.getText().toString().trim();
        int sexid = message_sex.getCheckedRadioButtonId();
        String sex;
        if(sexid == R.id.female){
            sex = "female";
        }else{
            sex = "male";
        }
        String birthday = message_birthday.getText().toString().trim();
        String address = message_address.getText().toString().trim();
        String explain = message_explain.getText().toString().trim();

        userDB.addUserMessage(username, nickname, sex, birthday, address, explain, new userBackListener() {
            @Override
            public void showResult(boolean result,String message) {
                if(result){
                    Toast.makeText(AddUserMessageActivity.this, "添加信息成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddUserMessageActivity.this, LoginActivity.class));
                    finish();
                }else{
                    Toast.makeText(AddUserMessageActivity.this, "添加信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ContentResolver resolver = getContentResolver();
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri originalUri = intent.getData();
                try {
                    Bitmap originalBitmap = BitmapFactory.decodeStream(resolver
                            .openInputStream(originalUri));
                    // originalUri 为拿到的不完整Uri
                    bitmap = resizeImage(originalBitmap,100, 100);
                    originalUri.toString();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    message_image.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(AddUserMessageActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        float scale;

        if((float)width/(float)height > (float)w/(float)h){
            scale = (float)w/(float)width;
        }else{
            scale = (float)h/(float)height;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Intent myIntent;
            myIntent = new Intent(AddUserMessageActivity.this, LoginActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return false;
    }
}
