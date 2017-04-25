package com.example.jasper.ccxapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jasper on 2017/4/23.
 */

public class UUIDKeyUtil {
    public static String getUUIDKey(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uuidStr = UUID.randomUUID().toString();
        String uuidKey = uuidStr.substring(0, 8) + uuidStr.substring(9, 13) + uuidStr.substring(14, 18) + uuidStr.substring(19, 23) + uuidStr.substring(24);
        return  uuidKey;
    }
}
