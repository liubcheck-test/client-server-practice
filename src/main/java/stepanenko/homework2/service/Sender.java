package stepanenko.homework2.service;

import stepanenko.practice1.model.Message;

import java.net.InetAddress;

/**
 * @author Liubomyr Stepanenko
 */
public interface Sender {
    void sendMessage(byte[] mess, InetAddress target);
    void sendMessage(Message message, InetAddress target);
}
