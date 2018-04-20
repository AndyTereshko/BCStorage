package com.petproject.andy.bcstorage.utils;

import android.app.Dialog;
import android.view.WindowManager;

public class DialogUtils {

    // Prevent dialog dismiss when orientation changes
    public static void doKeepDialog(Dialog dialog){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }
}
