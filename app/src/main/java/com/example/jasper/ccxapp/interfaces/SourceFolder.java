package com.example.jasper.ccxapp.interfaces;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jasper on 2017/4/21.
 */

public interface SourceFolder {
    public static final File orginFolder = new File(Environment.getExternalStorageDirectory()+File.separator+"ccxfile");
    public static final File videoFolder = new File(orginFolder.getPath() + File.separator + "videofile");
    public static final File imageFolder = new File(orginFolder.getPath() + File.separator + "imagefile");
    public static final File voiceFolder = new File(orginFolder.getPath() + File.separator + "voicefile");
}
