package com.example.jasper.ccxapp.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class ShowProcessUtil {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity, String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(activity,
                    title, message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
