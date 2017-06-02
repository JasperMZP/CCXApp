package com.example.jasper.ccxapp.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.jasper.ccxapp.R;

public class FlashlightActivity extends AppCompatActivity {

    private Button flashlight;
    private boolean ifOpen;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        flashlight = (Button)findViewById(R.id.flashlight);
        ifOpen = false;

        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifOpen){
                    closeFlashlight();
                }else{
                    openFlashlight();
                }
            }
        });
    }

    private void closeFlashlight() {
        camera.stopPreview();
        camera.release();
        ifOpen = false;
        flashlight.setText("开启手电筒");
    }

    private void openFlashlight() {
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        ifOpen = true;
        flashlight.setText("关闭手电筒");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if(ifOpen){
                closeFlashlight();
            }
            this.finish();
        }
        return false;
    }
}
