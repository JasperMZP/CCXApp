package com.example.jasper.ccxapp.interfaces;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jasper on 2017/4/21.
 */

public interface SourceFolder {
    File orginFolder = new File(Environment.getExternalStorageDirectory()+File.separator+"ccxfile");
    File videoFolder = new File(orginFolder.getPath() + File.separator + "videofile");
    File imageFolder = new File(orginFolder.getPath() + File.separator + "imagefile");
    File voiceFolder = new File(orginFolder.getPath() + File.separator + "voicefile");
}
