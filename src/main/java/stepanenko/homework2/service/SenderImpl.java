package stepanenko.homework2.service;

import stepanenko.practice1.model.Message;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice3.exception.ServerException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class SenderImpl implements Sender {
    private static SenderImpl sender;

    public static SenderImpl getInstance() {
        if (sender == null) {
            sender = new SenderImpl();
        }
        return sender;
    }

    @Override
    public void sendMessage(byte[] mess, InetAddress target) {
        System.out.println(Arrays.toString(mess));
    }

    @Override
    public void sendMessage(Message message, InetAddress target) {
        try {
            byte[] messageInBytes = EncryptorImpl.getInstance().encryptPacketBytes(message);
            //ReceiverImpl.getInstance().receiveMessage(messageInBytes, (mes) -> {});
            Socket socket = new Socket(target, 1337);

        } catch (IOException e) {
            throw new ServerException("Can't send the message" + message, e);
        }
    }
}
