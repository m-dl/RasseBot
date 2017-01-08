package com.ceri.rassebot.tools;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ceri.rassebot.main.MainActivity;
import com.ceri.rassebot.socket.Client;

public class Tools {

    public final static String OPTIONS = "OPTIONS", IP = "IP", PORT = "PORT", SOCKET_PORT = "SOCKET_PORT", SPEED = "SPEED", WELCOME = "WELCOME";
    public final static String HTTP = "http://", PORT_SEPARATOR = ":", STREAM_HTTP = "/stream", WIDTH_HTTP = "?width=", HEIGHT_HTTP = "&height=";
    public final static String DEFAULT_IP = "192.168.42.1", DEFAULT_WELCOME = "Bonjour Maxime et Adrian, je suis Rasse Bote";
    public final static int DEFAULT_PORT = 8080, DEFAULT_SOCKET_PORT = 2000, DEFAULT_SPEED = 50;

    // display toast message
    public static void notifToast(String s) {
        Toast.makeText(MainActivity.getContext(), s, Toast.LENGTH_SHORT).show();
    }

    // display snackbar message
    public static void notifBar(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    // treat vocal command
    public static String treatVocalCommand(String request) {
        // camera - first to treat or there is robot command conflict
        if(request.contains(Client.CAMERA) && request.contains(Client.GAUCHE))
            return Client.CAMERA + " " + Client.GAUCHE;
        else if(request.contains(Client.CAMERA) && request.contains(Client.DROITE))
            return Client.CAMERA + " " + Client.DROITE;
        // robot
        else if(request.contains(Client.AVANCE))
            return Client.ROBOT + " " + Client.AVANCE;
        else if(request.contains(Client.ACCELERE))
            return Client.ROBOT + " " + Client.ACCELERE;
        else if(request.contains(Client.RALENTIS))
            return Client.ROBOT + " " + Client.RALENTIS;
        else if(request.contains(Client.RECULE) || request.contains(Client.ARRIERE))
            return Client.ROBOT + " " + Client.RECULE;
        else if(request.contains(Client.STOP))
            return Client.ROBOT + " " + Client.STOP;
        else if(request.contains(Client.GAUCHE))
            return Client.ROBOT + " " + Client.GAUCHE;
        else if(request.contains(Client.DROITE))
            return Client.ROBOT + " " + Client.DROITE;
        return null;
    }
}
