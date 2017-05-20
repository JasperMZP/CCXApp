package com.example.jasper.ccxapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jasper on 2017/5/20.
 */

public class GetCurrentTimeUtil {
    public static String getCurrentTime(Date date){
        DateFormat df = new SimpleDateFormat("yy年MM月dd日 HH:mm");
        return df.format(date);
    }
}
