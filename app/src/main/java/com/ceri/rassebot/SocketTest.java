package com.ceri.rassebot;

import java.lang.*;
import java.io.*;
import java.net.*;


public class SocketTest {
    private Socket socket = null;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public SocketTest(InetAddress address, int port) throws IOException
    {
        socket = new Socket(address, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void send(String msg) throws IOException
    {
        writer.write(msg, 0, msg.length());
        writer.flush();
    }

    public String recv() throws IOException
    {
        return reader.readLine();
    }

    public static void main(String[] args)
    {
        try {
            InetAddress host = InetAddress.getByName("192.168.42.1");
            SocketTest client = new SocketTest(host, 2000);

            client.send("Hello server.\n");
            String response = client.recv();
            System.out.println("Client received: " + response);
        }
        catch (IOException e) {
            System.out.println("Caught Exception: " + e.toString());
        }
    }
}