package stepanenko.practice3.service;

import java.net.Socket;
import stepanenko.homework2.service.ProcessorImpl;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver(clientSocket);
        byte[] bytes = receiver.receiveMessage();
        Packet packet = DecryptorImpl.getInstance().decryptPacket(bytes);
        Message response = ProcessorImpl.getInstance().processMessage(packet.getbMsg());
        receiver.sendMessage(EncryptorImpl.getInstance().encrypt(response));
    }
}
