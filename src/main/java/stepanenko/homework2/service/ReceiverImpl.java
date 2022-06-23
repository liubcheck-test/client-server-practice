package stepanenko.homework2.service;

import java.util.Random;
import stepanenko.homework2.model.Command;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice1.util.ByteConverter;

public class ReceiverImpl implements Receiver {
    private static int userId = 0;
    private static char name = 'a';

    @Override
    public void receiveMessage() {
        byte[] messageInBytes = EncryptorImpl.getInstance().encryptPacketBytes(generatedMessage());
        DecryptorImpl.getInstance().decryptPacket(messageInBytes);
    }

    private static Message generatedMessage() {
        int commandNumber = Command.ADD_PRODUCT_TO_GROUP.ordinal();
        userId++;
        byte[] message = ByteConverter.convertObjectToBytes(new Object[] {String.valueOf(name++)});
        return new Message(commandNumber, userId, message);
    }
}
