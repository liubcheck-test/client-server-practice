package stepanenko.homework2.service;

import stepanenko.homework2.model.Command;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice1.util.ByteConverter;

import java.util.function.Consumer;

public class ReceiverImpl implements Receiver {
    private static int userId = 0;
    private static char name = 'a';
    private static ReceiverImpl receiver;

    public static ReceiverImpl getInstance() {
        if (receiver == null) {
            receiver = new ReceiverImpl();
        }
        return receiver;
    }

    @Override
    public void receiveMessage() {
        byte[] messageInBytes = EncryptorImpl.getInstance().encryptPacketBytes(generatedMessage());
        DecryptorImpl.getInstance().decrypt(messageInBytes);
    }

    private static Message generatedMessage() {
        int commandNumber = Command.ADD_PRODUCT_GROUP.ordinal();
        userId++;
        byte[] message = ByteConverter.convertObjectToBytes(new Object[] {String.valueOf(name++)});
        return new Message(commandNumber, userId, message);
    }
}
