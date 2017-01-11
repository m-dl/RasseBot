package com.ceri.rassebot.socket;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSender {

    private Socket socket;
    private String ip;
    private int port;

    public final static String GAUCHE = "gauche", DROITE = "droite",
            ROBOT = "robot", CAMERA = "camera", CAMERA2 = "caméra", AVANCE = "avance", RECULE = "recule", RECULE2 = "recul", ARRIERE = "arrière", STOP = "stop",
            ACCELERE = "accelere", ACCELERE2 = "accélère", RALENTIS = "ralentis", RALENTIS2 = "ralenti", SPEED = "speed";

    public ClientSender(String ip, int port, int speed) {
        this.ip = ip;
        this.port = port;
        // send default robot speed
        sendCommand(ClientSender.SPEED + " " + speed);
    }

    // send command to the server (robot)
    public void sendCommand(String command) {
        new SocketClient().execute(command);
    }

    // stop socket connection
    public void stopSocket() {
        try {
            if (socket != null) {
                this.socket.close();
                this.socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SocketClient extends AsyncTask<String, Object, Void> {

        protected Void doInBackground(String... args) {
            if(socket == null) {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                System.out.println("sending: " + args[0]);
                out.println(args[0]);
                out.flush();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}