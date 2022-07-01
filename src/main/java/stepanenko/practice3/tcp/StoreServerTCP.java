package stepanenko.practice3.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import stepanenko.homework2.service.Receiver;
import stepanenko.practice1.model.Message;
import stepanenko.practice3.exception.ServerException;
import stepanenko.practice3.service.ClientHandler;

public class StoreServerTCP implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    public StoreServerTCP(int port, int maxConnectionsNumber) {
        this.executorService = Executors.newFixedThreadPool(maxConnectionsNumber);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new ServerException("Can't load the server", e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                executorService.execute(new ClientHandler(serverSocket.accept()));
            }
        } catch (IOException e) {
            throw new ServerException("Can't start the server", e);
        } finally {
            executorService.shutdown();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Can't close the server socket", e);
        }
    }
}
