package com.ceri.rassebot.socket;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import com.ceri.rassebot.main.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientReceiver {

    private Socket socket;
    private String ip;
    private int port;

    public ClientReceiver(String ip, int port) {
        this.ip = ip;
        this.port = port;
        receiveCommand();
    }

    // receive command from the server (robot) - opencv objects
    public void receiveCommand() {
        new SocketClient().execute();
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

    public class SocketClient extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... args) {

            new Thread(new Runnable() {
                public void run() {
                    runListener();
                }
            }).start();
            return null;
        }

        public void runListener() {
            String response = "";
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
                System.out.println("Starting listening OpenCV");
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    response = in.readLine();
                    System.out.println("response: " + response);
                    if (response != null && !response.equals("")) {
                        MainActivity.textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null, null);
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}