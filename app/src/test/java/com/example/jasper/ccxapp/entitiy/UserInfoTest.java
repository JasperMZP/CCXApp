package com.example.jasper.ccxapp.entitiy;

import static org.junit.Assert.*;
import com.example.jasper.ccxapp.entitiy.UserInfo;

//UserInfo 里的一些函数没有没调用 ，故不对其进行单元测试
public class UserInfoTest extends UserInfo {
    private String  notename = "abc";
    private String  noteText = "abc";
    private long birthday = 19960818;

    //这里测试getNotename()方法
    public void testgetNotename() throws Exception{
        assertEquals(getNotename(),"abc");
        assertEquals(getNotename(),"aaa");
    }

    public void testgetNoteText() throws Exception{
        assertEquals(getNoteText(),"abc");
        assertEquals(getNoteText(),"aaa");
    }

    public void testgetBirthday() throws Exception{
        assertEquals(getBirthday(),19960818);
        assertEquals(getBirthday(),19421321);
    }

    public void testgetBlacklist() throws Exception{

        assertEquals(getBlacklist(),0);
    }

    public void testgetNoDisturb() throws Exception{
        assertEquals(getNoDisturb(),0);
    }

    public void testisFriend() throws Exception{
        //先测试能否得到正确的结果
        assertEquals(isFriend(),false);
        //再测试如果是错误的结果
        assertEquals(isFriend(),true);
    }

    public void testgetAppKey() throws Exception{
        assertEquals(getAppKey(),null);
    }

//下面开始是测试set方法的测试用例

    public void testsetBirthday(long l) throws Exception{
        //String br = String.valueOf(19960818);
        long br = 19960810;
        //因为这里setBirthday 把long型数据转为String 所以我们要测试其是否成功转化。
        setBirthday(l);
        assertEquals(l,br);
    }


}
