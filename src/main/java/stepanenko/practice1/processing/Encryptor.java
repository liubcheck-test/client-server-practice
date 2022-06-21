package stepanenko.practice1.processing;

import java.nio.ByteBuffer;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.util.CRC16Util;
import stepanenko.practice1.util.CipherUtil;

/**
 * @author Liubomyr Stepanenko
 */
public class Encryptor {
    private static final int DEFAULT_CAPACITY = 18;

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
}
