package stepanenko.practice1.processing;

import stepanenko.homework2.service.SenderImpl;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.util.CRC16Util;
import stepanenko.practice1.util.CipherUtil;

import java.nio.ByteBuffer;

/**
 * @author Liubomyr Stepanenko
 */
public class EncryptorImpl implements Encryptor {
    private static EncryptorImpl encryptor;
    private static final int DEFAULT_CAPACITY = 18;

    public static EncryptorImpl getInstance() {
        if (encryptor == null) {
            encryptor = new EncryptorImpl();
        }
        return encryptor;
    }

    public byte[] encryptPacketBytes(Message message) {
        byte[] messageBytes = encryptMessage(message);
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY + messageBytes.length);
        byte[] packetBytes = new byte[byteBuffer.remaining()];
        byteBuffer.put(Packet.B_MAGIC)
                .put((byte) DEFAULT_CAPACITY)
                .putLong(Packet.PACKET_ID++)
                .putInt(messageBytes.length);
        byte[] header = new byte[Packet.HEADER_LENGTH];
        byteBuffer.rewind().get(header);
        short crc16_start = CRC16Util.crc(header), crc16_end = CRC16Util.crc(messageBytes);
        byteBuffer.putShort(crc16_start)
                .put(messageBytes)
                .putShort(crc16_end)
                .rewind()
                .get(packetBytes);
        return packetBytes;
    }

    public byte[] encryptMessage(Message message) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES * 2
                    + message.getMessage().length);
            byte[] messageBytes = new byte[byteBuffer.remaining()];
            byteBuffer.putInt(message.getCType())
                    .putInt(message.getUserId())
                    .put(message.getMessage())
                    .rewind()
                    .get(messageBytes);
            return CipherUtil.getEncryptCipher().doFinal(messageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Can't encrypt the message", e);
        }
    }

    @Override
    public byte[] encrypt(Message message) {
        byte[] encryptedMessage =  encryptMessage(message);
        SenderImpl.getInstance().sendMessage(encryptedMessage, null);
        return encryptedMessage;
    }
}
