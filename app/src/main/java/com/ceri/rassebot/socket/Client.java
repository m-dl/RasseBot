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

    public final static String GAUCHE = "gauche", DROITE = "droite", HAUT = "haut", BAS = "bas",
            ROBOT = "robot", CAMERA = "camera", AVANCE = "avance", RECULE = "recule", ARRIERE = "arriere", STOP = "stop",
            ACCELERE = "accelere", RALENTIS = "ralentis", CENTRE = "centre", MILIEU = "milieu", SPEED = "speed";

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        //new Thread(new ClientThread()).start();
    }

    // send command to the server (robot)
    public void sendCommand(String command) {
        new SocketClient().execute(command);
    }

//    // send command to the server (robot)
//    public void sendCommand(String command) {
//        try {
//            if (socket != null) {
//                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//                out.println(command);
//                out.flush();
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String response = in.readLine();
//                System.out.println(response);
//                //MainActivity.textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null, null);
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // stop socket connection
//    public void stopSocket() {
//        try {
//            if (socket != null) {
//                this.socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // start connection
//    class ClientThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                InetAddress serverAddr = InetAddress.getByName(ip);
//                socket = new Socket(serverAddr, port);
//            } catch (UnknownHostException e1) {
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    public class SocketClient extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {
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
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                System.out.println("response: " + response);
                //MainActivity.textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
//            } finally {
//                if (socket != null) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
            }

            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground

            if (result != null) {
            }
        }
    }
}