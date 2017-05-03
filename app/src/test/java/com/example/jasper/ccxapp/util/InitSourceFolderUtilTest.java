package com.example.jasper.ccxapp.util;


import com.example.jasper.ccxapp.interfaces.SourceFolder;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.jasper.ccxapp.util.InitSourceFolderUtil;

public class InitSourceFolderUtilTest implements SourceFolder {

    @Test
    public void createFileDirsTest()  throws Exception {

        InitSourceFolderUtil.createFileDirs();
        assertTrue(orginFolder.exists());
        assertTrue(videoFolder.exists());
        assertTrue(imageFolder.exists());
        assertTrue(voiceFolder.exists());
    }
}