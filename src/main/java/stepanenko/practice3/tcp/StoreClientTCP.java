package stepanenko.practice3.tcp;

import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice3.exception.ProcessingException;
import stepanenko.practice3.exception.ServerException;
import stepanenko.practice3.service.Receiver;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class StoreClientTCP implements Runnable {
    private final int port;
    private Socket socket;
    private final Message message;
    private static final String LOCAL_HOST = "localhost";
    private static final int CONNECTION_TIMEOUT = 400, MAX_CONNECTION_ATTEMPTS = 3;

    public StoreClientTCP(int port, Message message) {
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            connectClient();
            Receiver receiver = new Receiver(socket);
            receiver.sendMessage(EncryptorImpl.getInstance().encryptPacketBytes(message));
            byte[] responseBytes = receiver.receiveMessage();
            Packet packet = DecryptorImpl.getInstance().decryptPacket(responseBytes);
            System.out.println("Server response: " + packet.getbMsg());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connectClient() {
        int connectionAttempts = 0;
        while (true) {
            try {
                socket = new Socket(LOCAL_HOST, port);
                return;
            } catch (ConnectException e) {
                if (connectionAttempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new ProcessingException("Can't do connection", e);
                }
                try {
                    Thread.sleep(CONNECTION_TIMEOUT +
                            CONNECTION_TIMEOUT * connectionAttempts);
                } catch (InterruptedException ex) {
                    throw new ProcessingException("Thread sleep invalidation", e);
                }
                connectionAttempts++;
            } catch (IOException e) {
                throw new ServerException("Can't connect the client");
            }
        }
    }
}
