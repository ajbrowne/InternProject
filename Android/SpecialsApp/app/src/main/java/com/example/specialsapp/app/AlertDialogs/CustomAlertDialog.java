package com.example.specialsapp.app.AlertDialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Allows for one line, easy Alert Dialog creation
 */
public class CustomAlertDialog extends AlertDialog.Builder {

    public CustomAlertDialog(Context context, String title, String message) {
        super(context);
        setTitle(title);
        setMessage(message);
        setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
