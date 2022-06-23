package stepanenko.homework2.service;

import java.net.InetAddress;
import java.util.Arrays;

public class SenderImpl implements Sender{
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
}
