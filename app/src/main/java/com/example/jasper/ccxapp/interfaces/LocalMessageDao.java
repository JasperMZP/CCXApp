package com.example.jasper.ccxapp.interfaces;

import com.example.jasper.ccxapp.db.LocalShowMessageDaoImpl;
import com.example.jasper.ccxapp.entitiy.CommentItemModel;
import com.example.jasper.ccxapp.entitiy.ShowItemModel;

import java.util.List;

/**
 * Created by Jasper on 2017/6/2.
 */

public interface LocalMessageDao extends MessageDBSQL, ShowType {
    LocalShowMessageDaoImpl open();

    void close();

    void insertShow(ShowItemModel showItem, String showType);

    void insertComment(CommentItemModel commentItem);

    List<ShowItemModel> readShowItemList();

    List<CommentItemModel> readCommentItemList();

}
