package com.example.jasper.ccxapp.util;

import com.example.jasper.ccxapp.entitiy.ShowItemModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasper on 2017/5/21.
 */

public class ShowMsgListOrderUtil {
    public static List<ShowItemModel> showMsgListOrder(List<ShowItemModel> showItemList) {
        DateFormat df = new SimpleDateFormat("yy年MM月dd日 HH:mm");
        ShowItemModel showItemTemp = null;
        for (int i=showItemList.size()-1;i>0;i--){
            for (int j=0;j<i;++j){
                try {
                    Date date1 = df.parse(showItemList.get(j+1).getShowTime());
                    Date date2 = df.parse(showItemList.get(j).getShowTime());
                    if (date1.after(date2)){
                        showItemTemp=showItemList.get(j);
                        showItemList.set(j,showItemList.get(j+1));
                        showItemList.set(j+1,showItemTemp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return  showItemList;
    }
}
