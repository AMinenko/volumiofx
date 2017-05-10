package com.anmi.volumiofx;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class ClientSocket {
    private Consumer<String> consumer;
    private ConnectionThread connectionThread = new ConnectionThread();
    private ClientSocket clientSocket = this;
    private Socket ioSocket;

    public void socketIoConnect() throws URISyntaxException {
        ioSocket = IO.socket("http://127.0.0.1:3001/");
        ioSocket.connect();
        ioSocket.on(Socket.EVENT_MESSAGE, objects -> consumer.accept(objects.toString()));
    }




    public ClientSocket(Consumer<String> consumer) {
        this.consumer = consumer;
    }


    CountDownLatch latch = new CountDownLatch(1);
    private Session session;

    public void startConnection() {
        connectionThread.setDaemon(true);
        connectionThread.setName("ClientSocketThread");
        connectionThread.start();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        this.session = session;
        latch.countDown();
    }

    @OnMessage
    public void onText(String message, Session session) {
        consumer.accept(message);
        System.out.println("Message received from server:" + message);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }

    public void sendMessage(String str) throws IOException {
       // session.getBasicRemote().sendText(str);
        ioSocket.send(str);
    }

    private class ConnectionThread extends Thread {

        @Override
        public void run() {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.connectToServer(clientSocket, new URI("ws://192.168.1.201:3000/ioSocket.io/?EIO=3&transport=websocket"));
            } catch (IOException e) {
                consumer.accept("Connection cosed");
            } catch (DeploymentException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}