package com.ceri.rassebot.tools;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ceri.rassebot.main.MainActivity;

public class Tools {

    public final static String OPTIONS = "OPTIONS", IP = "IP", PORT = "PORT", SPEED = "SPEED";
    public final static String DEFAULT_IP = "192.168.1.254";
    public final static int DEFAULT_PORT = 8080, DEFAULT_SPEED = 1;


    // display toast message
    public static void notifToast(String s) {
        Toast.makeText(MainActivity.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    // display toast message
    public static void notifBar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }
}
