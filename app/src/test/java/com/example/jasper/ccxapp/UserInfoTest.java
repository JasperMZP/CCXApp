package com.example.jasper.ccxapp;

import com.example.jasper.ccxapp.entitiy.UserInfo;

import static org.junit.Assert.*;


//UserInfo 里的一些函数没有没调用 ，故不对其进行单元测试
public class UserInfoTest extends UserInfo {
    private String  notename = "abc";
    private String  noteText = "abc";
    private long birthday = 19960818;

    //这里测试getNotename()方法
    public void testgetNotename(){
        assertEquals(getNotename(),"abc");
        assertEquals(getNotename(),"aaa");
    }

    public void testgetNoteText(){
        assertEquals(getNoteText(),"abc");
        assertEquals(getNoteText(),"aaa");
    }

    public void testgetBirthday(){
        assertEquals(getBirthday(),19960818);
        assertEquals(getBirthday(),19421321);
    }

    public void testgetBlacklist(){

        assertEquals(getBlacklist(),0);
    }

    public void testgetNoDisturb(){
        assertEquals(getNoDisturb(),0);
    }

    public void testisFriend(){
        //先测试能否得到正确的结果
        assertEquals(isFriend(),false);
        //再测试如果是错误的结果
        assertEquals(isFriend(),true);
    }

    public void testgetAppKey(){
        assertEquals(getAppKey(),null);
    }

//下面开始是测试set方法的测试用例

    public void testsetBirthday(long l){
        String br = String.valueOf(19960818);
        //因为这里setBirthday 把long型数据转为String 所以我们要测试其是否成功转化。
        setBirthday(l);
        assertEquals(l,br);
    }


}
