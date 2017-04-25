//package com.example.jasper.ccxapp.util;
//
//import android.net.Uri;
//
//import com.example.jasper.ccxapp.interfaces.FileType;
//import com.example.jasper.ccxapp.interfaces.ShowType;
//import com.example.jasper.ccxapp.interfaces.SourceFolder;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * Created by Jasper on 2017/4/21.
// */
//
//public class OutputMediaFileUriUtil implements FileType, ShowType, SourceFolder {
//
//    public static Uri getOutputMediaFileUri(String type) {
//        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String uuidStr = UUID.randomUUID().toString();
//        String uuidKey = uuidStr.substring(0, 8) + uuidStr.substring(9, 13) + uuidStr.substring(14, 18) + uuidStr.substring(19, 23) + uuidStr.substring(24);
//        switch (type) {
//            case IMAGE:
//                File imageFile = new File(imageFolder.getPath() + File.separator + SHOW + "_" + IMAGE + "_" + timeStamp + "_" + uuidKey + "_" + ".jpg");
//                return Uri.fromFile(imageFile);
//            case VIDEO:
//                File videoFile = new File(videoFolder.getPath() + File.separator + SHOW + "_" + VIDEO + "_" + timeStamp + "_" + uuidKey + "_" + ".mp4");
//                return Uri.fromFile(videoFile);
//            case VOICE:
//                File voiceFile = new File(voiceFolder.getPath() + File.separator + COMMENT + "_" + VOICE + "_" + timeStamp + "_" + "UUIDKEY" + "_" + ".mp3");
//                return Uri.fromFile(voiceFile);
//        }
//
//        return null;
//    }
//}
