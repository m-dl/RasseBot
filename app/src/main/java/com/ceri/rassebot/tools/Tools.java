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

    // convert angle to direction
    public static String angleToDirection(int angle) {
        if(angle <= 115 && angle >= 65)
            return Client.HAUT;
        else if(angle <= 295 && angle >= 245)
            return Client.BAS;
        else if(angle <= 205 && angle >= 145)
            return Client.GAUCHE;
        else if((angle <= 25 && angle >= 0) || (angle <= 360 && angle >= 335))
            return Client.DROITE;
        else if(angle <= 145 && angle >= 115)
            return Client.HAUT + " " + Client.GAUCHE;
        else if(angle <= 65 && angle >= 25)
            return Client.HAUT + " " + Client.DROITE;
        else if(angle <= 245 && angle >= 205)
            return Client.BAS + " " + Client.GAUCHE;
        else if(angle <= 335 && angle >= 295)
            return Client.BAS + " " + Client.DROITE;
        return null;
    }

    // convert angle to bidirection
    public static String angleToBidirection(int angle) {
        if(angle <= 270 && angle >= 90)
            return Client.GAUCHE;
        else if(angle <= 360 && angle >= 0)
            return Client.DROITE;
        return null;
    }

    // treat vocal command
    public static String treatVocalCommand(String request) {
        // camera - first to treat or there is robot command conflict
        if(request.contains(Client.CAMERA) && request.contains(Client.GAUCHE))
            return Client.CAMERA + " " + Client.GAUCHE;
        else if(request.contains(Client.CAMERA) && request.contains(Client.DROITE))
            return Client.CAMERA + " " + Client.DROITE;
        else if(request.contains(Client.CAMERA) && (request.contains(Client.CENTRE) || (request.contains(Client.MILIEU))))
            return Client.CAMERA + " " + Client.CENTRE;
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
        // TODO: gauche / droite mais repartir ensuite, selon si on avance ou recule ou on est sur place aussi ?
        // TODO: et gérer la vitesse avec speed du JStick
        else if(request.contains(Client.GAUCHE))
            return Client.ROBOT + " " + Client.GAUCHE;
        else if(request.contains(Client.DROITE))
            return Client.ROBOT + " " + Client.DROITE;
        return null;
    }
}
