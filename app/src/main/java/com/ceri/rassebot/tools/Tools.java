package com.ceri.rassebot.tools;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ceri.rassebot.main.MainActivity;
import com.ceri.rassebot.socket.ClientSender;

public class Tools {

    public final static String OPTIONS = "OPTIONS", IP = "IP", PORT = "PORT", SOCKET_PORT = "SOCKET_PORT", SPEED = "SPEED", WELCOME = "WELCOME";
    public final static String HTTP = "http://", PORT_SEPARATOR = ":", STREAM_HTTP = "/stream", WIDTH_HTTP = "?width=", HEIGHT_HTTP = "&height=";
    public final static String DEFAULT_IP = "192.168.42.1", DEFAULT_WELCOME = "Bonjour Maxime et Adrian, je suis Rasse Bote";
    public final static int DEFAULT_PORT = 8080, DEFAULT_SOCKET_SENDER_PORT = 2000, DEFAULT_SOCKET_RECEIVER_PORT = 2001, DEFAULT_SPEED = 50;

    // display toast message
    public static void notifToast(String s) {
        Toast.makeText(MainActivity.getContext(), s, Toast.LENGTH_LONG).show();
    }

    // display snackbar message
    public static void notifBar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    // treat vocal command
    public static String treatVocalCommand(String request) {
        // camera - first to treat or there is robot command conflict
        if(request.contains(ClientSender.CAMERA2) && request.contains(ClientSender.GAUCHE))
            return ClientSender.CAMERA + " " + ClientSender.GAUCHE;
        else if(request.contains(ClientSender.CAMERA2) && request.contains(ClientSender.DROITE))
            return ClientSender.CAMERA + " " + ClientSender.DROITE;
        // robot
        else if(request.contains(ClientSender.AVANCE))
            return ClientSender.ROBOT + " " + ClientSender.AVANCE;
        else if(request.contains(ClientSender.ACCELERE2))
            return ClientSender.ROBOT + " " + ClientSender.ACCELERE;
        else if(request.contains(ClientSender.RALENTIS2))
            return ClientSender.ROBOT + " " + ClientSender.RALENTIS;
        else if(request.contains(ClientSender.RECULE2) || request.contains(ClientSender.ARRIERE))
            return ClientSender.ROBOT + " " + ClientSender.RECULE;
        else if(request.contains(ClientSender.STOP))
            return ClientSender.ROBOT + " " + ClientSender.STOP;
        else if(request.contains(ClientSender.GAUCHE))
            return ClientSender.ROBOT + " " + ClientSender.GAUCHE;
        else if(request.contains(ClientSender.DROITE))
            return ClientSender.ROBOT + " " + ClientSender.DROITE;
        return null;
    }
}
