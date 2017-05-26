package com.example.jasper.ccxapp;


import com.example.jasper.ccxapp.interfaces.ShowType;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ShowTypeTest implements ShowType {

    public void valueTest() throws Exception{
        assertEquals(SHOW,"show");
        assertEquals(SHOW,"SHOW");
        assertEquals(COMMENT,"comment");
        assertEquals(COMMENT,"COMMENT");
    }
}
