package stepanenko.homework2.service;

import java.net.InetAddress;

/**
 * @author Liubomyr Stepanenko
 */
public interface Sender {
    void sendMessage(byte[] mess, InetAddress target);
}
