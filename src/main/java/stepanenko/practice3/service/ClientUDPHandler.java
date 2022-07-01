package stepanenko.practice3.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import stepanenko.homework2.service.ProcessorImpl;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice3.exception.ProcessingException;

public class ClientUDPHandler implements Runnable{
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public ClientUDPHandler(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }

    @Override
    public void run() {
        Packet packet = DecryptorImpl.getInstance().decryptPacket(datagramPacket.getData());
        Message message = packet.getbMsg();
        Message response = ProcessorImpl.getInstance().processMessage(message);
        byte[] encryptedResponse = EncryptorImpl.getInstance().encryptPacketBytes(response);
        try {
            datagramSocket = new DatagramSocket();
            DatagramPacket responsePacket = new DatagramPacket(encryptedResponse,
                    encryptedResponse.length, datagramPacket.getAddress(), datagramPacket.getPort());
            datagramSocket.send(responsePacket);
        } catch (IOException e) {
            throw new ProcessingException("Can't work with datagram socket", e);
        } finally {
            datagramSocket.close();
        }
    }
}
