package stepanenko.practice3.udp;

import stepanenko.practice3.exception.ServerException;
import stepanenko.practice3.service.ClientHandler;
import stepanenko.practice3.service.ClientUDPHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreServerUDP implements Runnable {
    public static final int BUFFER_LENGTH = 1024;
    private DatagramSocket datagramSocket;
    private ExecutorService executorService;

    public StoreServerUDP(int port, int maxConnectionsNumber) {
        this.executorService = Executors.newFixedThreadPool(maxConnectionsNumber);
        try {
            this.datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new ServerException("Can't create datagram socket", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] buffer = new byte[BUFFER_LENGTH];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, BUFFER_LENGTH);
            try {
                datagramSocket.receive(datagramPacket);
            } catch (IOException e) {
                executorService.shutdown();
                break;
            }
            executorService.execute(new ClientUDPHandler(datagramPacket));
        }
    }
}
