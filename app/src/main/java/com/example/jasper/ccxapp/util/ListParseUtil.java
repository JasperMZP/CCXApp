package com.example.jasper.ccxapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 2017/5/21.
 */

public class ListParseUtil {
    public static String strListToString(List<String> list) {
        String strAll = "";
        for (String str : list) {
            strAll += str+",";
        }
        return strAll;
    }

    public static String longListToString(List<Long> longList){
        String strAll="";
        for (Long l:longList){
            strAll += Long.toString(l)+",";
        }
        return strAll;
    }

    public static List<Long> stringToLongList(String longStr){
        List<Long> longList = new ArrayList<Long>();
        String[] strArr = longStr.split(",");
        for (int i=0;i<strArr.length;i++){
            longList.add(Long.parseLong(strArr[i]));
        }
        return longList;
    }

    public static ArrayList<String> stringToStrList(String strAll){
        ArrayList<String> strlist = new ArrayList<String>();
        String[] strArr = strAll.split(",");
        for (int i=0;i<strArr.length;i++){
            strlist.add(strArr[i]);
        }
        return strlist;
    }
}
