package com.ceri.rassebot.tools;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ceri.rassebot.main.MainActivity;
import com.ceri.rassebot.socket.Client;

public class Tools {

    public final static String OPTIONS = "OPTIONS", IP = "IP", PORT = "PORT", SPEED = "SPEED", WELCOME = "WELCOME";
    public final static String HTTP = "http://", PORT_SEPARATOR = ":", STREAM_HTTP = "/stream", WIDTH_HTTP = "?width=", HEIGHT_HTTP = "&height=";
    public final static String DEFAULT_IP = "192.168.1.254", DEFAULT_WELCOME = "Bonjour Maxime et Adrian, je suis Rasse Bote";
    public final static int DEFAULT_PORT = 8080, DEFAULT_SPEED = 1;


    // display toast message
    public static void notifToast(String s) {
        Toast.makeText(MainActivity.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    // display snackbar message
    public static void notifBar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    // convert angle to direction
    public static String angleToDirection(int angle) {
        if(angle < 90)
            return Client.DROITE;
        if(angle < 180)
            return Client.DROITE;
        if(angle < 270)
            return Client.DROITE;
        if(angle < 360)
            return Client.DROITE;
        return null;
    }

    // convert angle to direction
    public static String angleToBidirection(int angle) {
        if(angle <= 270 && angle >= 90)
            return Client.GAUCHE;
        else if(angle <= 360 && angle >= 0)
            return Client.DROITE;
        return null;
    }
}
