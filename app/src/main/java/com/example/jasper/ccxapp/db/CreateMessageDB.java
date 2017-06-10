package com.example.jasper.ccxapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.jasper.ccxapp.interfaces.MessageDBSQL;
import com.example.jasper.ccxapp.interfaces.MessageType;

/**
 * Created by Jasper on 2017/5/20.
 */

public class CreateMessageDB implements MessageDBSQL, MessageType {

    private final Context context;
    private MsgDBHelper msgDBHelper;
    private SQLiteDatabase db;

    public CreateMessageDB(Context context) {
        this.context = context;
        this.msgDBHelper = new MsgDBHelper(context);
    }

    public static class MsgDBHelper extends SQLiteOpenHelper {

        public MsgDBHelper(Context context) {
            super(context, MESSAGE_DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("test","创建数据库");
            db.execSQL(CREATE_SHOW_TEXT);
            db.execSQL(CREATE_SHOW_IMAGE);
            db.execSQL(CREATE_SHOW_VIDEO);
            db.execSQL(CREATE_COMMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public CreateMessageDB open() throws SQLException {
        this.db = this.msgDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.msgDBHelper.close();
    }

}
