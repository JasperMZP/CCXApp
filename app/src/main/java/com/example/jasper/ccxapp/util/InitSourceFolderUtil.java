package com.example.jasper.ccxapp.util;

import com.example.jasper.ccxapp.interfaces.SourceFolder;

import java.io.File;

/**
 * Created by Jasper on 2017/4/21.
 */

public class InitSourceFolderUtil implements SourceFolder {

    public static void createFileDirs() {

        if (!orginFolder.exists()) {
            orginFolder.mkdir();

            if (!videoFolder.exists()) {
                videoFolder.mkdir();
            }

            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            if (!voiceFolder.exists()) {
                voiceFolder.mkdir();
            }
        }
    }
}
