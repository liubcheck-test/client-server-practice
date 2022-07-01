package stepanenko.practice3.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import stepanenko.practice3.exception.ProcessingException;

public class Receiver {
    private static final int ATTEMPTS_NUMBER = 20;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public Receiver(Socket socket) {
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new ProcessingException("Can't get the stream", e);
        }
    }

    public byte[] receiveMessage() {
        byte[] messageInBytes = new byte[0];
        int timeForWaiting = 0;
        try {
            while(true) {
                if (inputStream.available() == 0) {
                    if (++timeForWaiting > ATTEMPTS_NUMBER) {
                        return messageInBytes;
                    }
                    Thread.sleep(100);
                    continue;
                }
                byte[] additionalBytes = inputStream.readNBytes(inputStream.available());
                messageInBytes = bytesConcatenation(messageInBytes, additionalBytes);
                return messageInBytes;
            }
        } catch (IOException | InterruptedException e) {
            throw new ProcessingException("Can't receive the message correctly", e);
        }
    }

    public void sendMessage(byte[] messageInBytes) {
        try {
            outputStream.write(messageInBytes);
        } catch (IOException e) {
            throw new ProcessingException("Can't send the message", e);
        }
    }

    public void stop() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new ProcessingException("Can't close some process", e);
        }
    }

    private byte[] bytesConcatenation(byte[] bytes, byte[] additionalBytes) {
        byte[] result = new byte[bytes.length + additionalBytes.length];
        System.arraycopy(bytes, 0, result, 0, additionalBytes.length);
        System.arraycopy(additionalBytes, 0, result, bytes.length, additionalBytes.length);
        return result;
    }
}
