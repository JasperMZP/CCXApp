package com.example.jasper.ccxapp.list;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 2017/6/10.
 */

public class ListTest {

    @Test
    public void listTest(){
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        List<String> list2 ;//=new ArrayList<String>();
        list2=list;
        System.out.println(list.get(0));
       // list2.add(list.get(0));
        list.clear();
        //System.out.println(list2.get(0));
    }
}
