package stepanenko.practice3.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice3.exception.ProcessingException;

public class StoreClientUDP implements Runnable {
    private static int userId = 100;
    private final int port;
    private final Message message;
    private final DatagramSocket datagramSocket;
    private final InetAddress inetAddress = InetAddress.getLocalHost();

    public StoreClientUDP(int port, Message message) throws UnknownHostException {
        try {
            this.port = port;
            this.message = message;
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new ProcessingException("Can't create datagram socket", e);
        }
    }

    @Override
    public void run() {
        boolean packetIsProcessed = false;
        do {
            message.setbUserId(userId++);
            byte[] messageInBytes = EncryptorImpl.getInstance().encryptMessage(message);
            DatagramPacket datagramPacket = new DatagramPacket(messageInBytes,
                    messageInBytes.length, inetAddress, port);
            try {
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                throw new ProcessingException("Can't send datagram packet", e);
            }
            while (true) {
                byte[] buffer = new byte[StoreServerUDP.BUFFER_LENGTH];
                DatagramPacket responsePacket = new DatagramPacket(buffer,
                        StoreServerUDP.BUFFER_LENGTH);
                try {
                    datagramSocket.receive(responsePacket);
                } catch (IOException e) {
                    datagramSocket.close();
                    break;
                }
                Packet packet = DecryptorImpl.getInstance()
                        .decryptPacket(responsePacket.getData());
                if (packet.getbMsg().getUserId() == userId) {
                    packetIsProcessed = true;
                }
                System.out.println("Server response: " + packet.getbMsg());
                break;
            }
        } while (!packetIsProcessed);
    }
}
