package com.example.specialsapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by brownea on 6/17/14.
 */
public class MyAlertDialog extends AlertDialog.Builder {

    public MyAlertDialog(Context context, String title, String message){
        super(context);
        setTitle(title);
        setMessage(message);
        setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
