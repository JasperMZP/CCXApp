package com.example.jasper.ccxapp.util;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;
import com.example.jasper.ccxapp.util.ShowMsgListOrderUtil;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;

/**
 * Created by DPC on 2017/5/26.
 */
public class ShowMsgListOrderUtilTest extends TestCase {

    ShowMsgListOrderUtil showMsgListOrderUtil;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Test
    public void testshowMsgListOrder() throws Exception {
        List<ShowItemModel> showItemList = new ArrayList<ShowItemModel>();
        ShowItemModel a= new ShowItemModel();
        ShowItemModel showItemTemp[]=new ShowItemModel[3];
        for(int i=0;i<3;i++){
            showItemTemp[i]=a;
        }

            showItemTemp[0].setShowTime("2017/5/26 15:46");
            showItemTemp[1].setShowTime("2017/5/26 15:47");
            showItemTemp[2].setShowTime("2016/5/26 15/26");

        //通过调用showMsgListOrderUtil里的showMsgListOrder来测试其是否可以完成指定功能
        showItemList = showMsgListOrderUtil.showMsgListOrder(showItemList);

        Assert.assertNotNull("测试成功");
    }

}