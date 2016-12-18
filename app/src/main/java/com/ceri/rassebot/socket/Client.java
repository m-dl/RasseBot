package com.ceri.rassebot.socket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket;
    private String ip;
    private int port;

    public final static String GAUCHE = "gauche", DROITE = "droite", HAUT = "haut", BAS = "bas",
    ROBOT = "robot", CAMERA = "camera";

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        new Thread(new ClientThread()).start();
    }

    public void sendCommand(String command) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(command);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}