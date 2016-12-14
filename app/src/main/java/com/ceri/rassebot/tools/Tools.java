package com.ceri.rassebot.tools;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ceri.rassebot.main.MainActivity;

public class Tools {

    // display toast message
    public static void notifToast(String s) {
        Toast.makeText(MainActivity.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    // display toast message
    public static void notifBar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }
}
