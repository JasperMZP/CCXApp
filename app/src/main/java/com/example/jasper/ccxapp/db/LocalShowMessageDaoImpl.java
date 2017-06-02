package com.example.jasper.ccxapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jasper.ccxapp.entitiy.CommentItemModel;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;
import com.example.jasper.ccxapp.interfaces.LocalMessageDao;
import com.example.jasper.ccxapp.util.ListParseUtil;
import com.example.jasper.ccxapp.util.ShowMsgListOrderUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 2017/5/20.
 */

public class LocalShowMessageDaoImpl implements LocalMessageDao {

    private final Context context;
    private MsgDBHelper msgDBHelper;
    private SQLiteDatabase db;

    private List<ShowItemModel> showItemModelList = new ArrayList<ShowItemModel>();
    private List<CommentItemModel> commentItemModelList = new ArrayList<CommentItemModel>();

    public LocalShowMessageDaoImpl(Context context) {
        this.context = context;
        this.msgDBHelper = new MsgDBHelper(this.context);
    }

    public static class MsgDBHelper extends SQLiteOpenHelper {


        public MsgDBHelper(Context context) {
            super(context, MESSAGE_DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public LocalShowMessageDaoImpl open() throws SQLException {
        this.db = this.msgDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.msgDBHelper.close();
    }

    public void insertShow(ShowItemModel showItem, String showType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("msgKey", showItem.getMsgKey());
        contentValues.put("groupBelongToList", ListParseUtil.longListToString(showItem.getGroupBelongToList()));
        contentValues.put("showUsername", showItem.getShowUsername());
        contentValues.put("showAvatar", showItem.getShowAvatar().getPath());
        contentValues.put("showTime", showItem.getShowTime());
        contentValues.put("showText", showItem.getShowText());
        if (showType.equals(SHOW_IMAGE)) {
            contentValues.put("showImagesList", ListParseUtil.strListToString(showItem.getShowImagesList()));
            db.insert(showImageTableName, null, contentValues);
        } else if (showType.equals(SHOW_VIDEO)) {
            contentValues.put("showVideo", showItem.getShowVideo());
            db.insert(showVideoTableName, null, contentValues);
        } else if (showType.equals(SHOW_TEXT)) {
            db.insert(showTextTableName, null, contentValues);
        }
    }

    public void insertComment(CommentItemModel commentItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("msgKey", commentItem.getMsgKey());
        contentValues.put("commKey", commentItem.getCommKey());
        contentValues.put("commentUsername", commentItem.getCommentUsername());
        contentValues.put("commentVoice", commentItem.getCommentVoice());
        contentValues.put("commentLength", commentItem.getCommentLength());
        contentValues.put("commentTime", commentItem.getCommentTime());

        db.insert(commentTableName, null, contentValues);
    }

    public List<ShowItemModel> readShowItemList() {
        readShowTextList();
        readShowImageList();
        readShowVideoList();
        return ShowMsgListOrderUtil.showMsgListOrder(showItemModelList);
    }

    public List<CommentItemModel> readCommentItemList() {
        Cursor cursor = db.query(commentTableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CommentItemModel commentItem = new CommentItemModel();
                commentItem.setMsgKey(cursor.getString(cursor.getColumnIndex("msgKey")));
                commentItem.setCommKey(cursor.getString(cursor.getColumnIndex("commKey")));
                commentItem.setCommentUsername(cursor.getString(cursor.getColumnIndex("commentUsername")));
                commentItem.setCommentVoice(cursor.getString(cursor.getColumnIndex("commentVoice")));
                commentItem.setCommentLength(cursor.getInt(cursor.getColumnIndex("commentLength")));
                commentItem.setCommentTime(cursor.getString(cursor.getColumnIndex("commentTime")));
                commentItemModelList.add(commentItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return commentItemModelList;
    }


    public void readShowTextList() {
        Cursor cursor = db.query(showTextTableName, null, null, null, null, null, null);
        Log.i("test", "Textnum" + cursor.getCount());
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ShowItemModel showTextItem = new ShowItemModel();
                    showTextItem.setMsgKey(cursor.getString(cursor.getColumnIndex("msgKey")));
                    showTextItem.setGroupBelongToList(ListParseUtil.stringToLongList(cursor.getString(cursor.getColumnIndex("groupBelongToList"))));
                    showTextItem.setShowUsername(cursor.getString(cursor.getColumnIndex("showUsername")));
                    showTextItem.setShowAvatar(new File(cursor.getString(cursor.getColumnIndex("showAvatar"))));
                    showTextItem.setShowText(cursor.getString(cursor.getColumnIndex("showText")));
                    showTextItem.setShowTime(cursor.getString(cursor.getColumnIndex("showTime")));
                    Log.i("test", "DBText" + cursor.getString(cursor.getColumnIndex("showText")));
                    showItemModelList.add(showTextItem);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    public void readShowImageList() {
        Cursor cursor = db.query(showImageTableName, null, null, null, null, null, null);
        Log.i("test", "Imagenum" + cursor.getCount());
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ShowItemModel showImageItem = new ShowItemModel();
                    showImageItem.setMsgKey(cursor.getString(cursor.getColumnIndex("msgKey")));
                    showImageItem.setGroupBelongToList(ListParseUtil.stringToLongList(cursor.getString(cursor.getColumnIndex("groupBelongToList"))));
                    showImageItem.setShowUsername(cursor.getString(cursor.getColumnIndex("showUsername")));
                    showImageItem.setShowAvatar(new File(cursor.getString(cursor.getColumnIndex("showAvatar"))));
                    showImageItem.setShowText(cursor.getString(cursor.getColumnIndex("showText")));
                    showImageItem.setShowTime(cursor.getString(cursor.getColumnIndex("showTime")));
                    showImageItem.setShowImagesList(ListParseUtil.stringToStrList(cursor.getString(cursor.getColumnIndex("showImagesList"))));
                    showItemModelList.add(showImageItem);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

    }

    public void readShowVideoList() {
        Cursor cursor = db.query(showVideoTableName, null, null, null, null, null, null);
        Log.i("test", "Videonum" + cursor.getCount());
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    ShowItemModel showVideoItem = new ShowItemModel();
                    showVideoItem.setMsgKey(cursor.getString(cursor.getColumnIndex("msgKey")));
                    showVideoItem.setGroupBelongToList(ListParseUtil.stringToLongList(cursor.getString(cursor.getColumnIndex("groupBelongToList"))));
                    showVideoItem.setShowUsername(cursor.getString(cursor.getColumnIndex("showUsername")));
                    showVideoItem.setShowAvatar(new File(cursor.getString(cursor.getColumnIndex("showAvatar"))));
                    showVideoItem.setShowText(cursor.getString(cursor.getColumnIndex("showText")));
                    showVideoItem.setShowTime(cursor.getString(cursor.getColumnIndex("showTime")));
                    showVideoItem.setShowVideo(cursor.getString(cursor.getColumnIndex("showVideo")));
                    showItemModelList.add(showVideoItem);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
    }
}
