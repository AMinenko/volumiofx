package com.anmi.volumiofx.scene;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class JavaSocket {

    private Socket socket;
    BufferedWriter out;
    BufferedReader in;
    Consumer<String> consumer;
    ConnectionThread thread;

    public JavaSocket(Consumer<String> consumer) {
        this.consumer = consumer;
        socket = new Socket();
        thread = new ConnectionThread();
        thread.start();

    }

    public void connect() throws IOException {
        socket.connect(new InetSocketAddress("localhost",6600),120000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void fireCommand(String command) throws IOException {
        checkReconnect();

        out.write(command, 0, command.length());
        out.newLine();
        out.flush();
    }

    private void checkReconnect() throws IOException {
        if (!socket.isConnected()){
            in.close();
            out.close();
            connect();
        }


    }

    private class ConnectionThread extends Thread {
        @Override
        public synchronized void run() {
            try {
                connect();
                while (true){
                    String message = in.readLine();
                    if(message != null){
                        consumer.accept(message.concat(System.lineSeparator()));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
