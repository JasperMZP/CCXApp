package com.example.jasper.ccxapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import cn.jpush.im.android.api.model.UserInfo;

import static org.junit.Assert.*;

/**
 * Created by DPC on 2017/5/10.
 */
public class FriendAdapterTest {
    private List<UserInfo> userInfos;

    @Test
    public void testgetCount() throws Exception {
        userInfos= null;
        assertEquals(userInfos,null);
    }

    @Test
    public void getItem() throws Exception {
        Objects a = null;
        assertEquals(a,null);
    }

    @Test
    public void getItemId() throws Exception {
        long position = 1234567;
        assertEquals(position,1234567);
    }

    @Test
    public void getView() throws Exception {

    }

}