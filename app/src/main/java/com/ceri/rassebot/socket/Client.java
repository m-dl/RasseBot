package com.ceri.rassebot.socket;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import com.ceri.rassebot.main.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket;
    private String ip;
    private int port;

    public final static String GAUCHE = "gauche", DROITE = "droite",
            ROBOT = "robot", CAMERA = "camera", CAMERA2 = "caméra", AVANCE = "avance", RECULE = "recule", RECULE2 = "recul", ARRIERE = "arrière", STOP = "stop",
            ACCELERE = "accelere", ACCELERE2 = "accélère", RALENTIS = "ralentis", RALENTIS2 = "ralenti", SPEED = "speed";

    public Client(String ip, int port, int speed) {
        this.ip = ip;
        this.port = port;
        // send default robot speed
        sendCommand(Client.SPEED + " " + speed);
    }

    // send command to the server (robot)
    public void sendCommand(String command) {
        new SocketClient().execute(command);
    }

    // receive command from the server (robot) - opencv objects
    public void receiveCommand() {
        new SocketClientReceiver().execute();
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

    public class SocketClientReceiver extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... args) {
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
                System.out.println("Debut ");
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    response = in.readLine();
                    System.out.println("response: " + response);
//                    if (response != null && !response.equals("")) {
//                        MainActivity.textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null, null);
//                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
//            System.out.println("response: " + result);
//            if (result != null && !result.equals("")) {
//                MainActivity.textToSpeech.speak(result, TextToSpeech.QUEUE_ADD, null, null);
//            }
        }
    }
}