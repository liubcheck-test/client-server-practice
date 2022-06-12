package stepanenko.practice.model;

import stepanenko.practice.exception.PacketException;
import stepanenko.practice.util.CRC16Util;
import stepanenko.practice.util.KeyUtil;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

public class PacketDecryptor {
    private static final byte bMagic = 0x13;
    private static final int HEADER_LENGTH = 13, FOOTER_OFFSET = 16;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16_start, wCrc16_end;
    private Message bMsg;

    public PacketDecryptor(byte[] bytes) {
        if (bytes[0] != bMagic) {
            throw new PacketException("Wrong packet start, must be "
                    + bMagic + "in hex format , have " + bytes[0] + " (in decimal format)");
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        this.bSrc = byteBuffer.get();
        this.bPktId = byteBuffer.getLong();
        this.wLen = byteBuffer.getInt();
        this.wCrc16_start = byteBuffer.getShort();

        byte[] header = ByteBuffer.wrap(bytes, 0, HEADER_LENGTH).array();
        short crc_start = CRC16Util.crc(header);
        if (this.wCrc16_start != crc_start) {
            throw new PacketException("Wrong CRC16 start, must be "
                    + crc_start + ", have " + this.wCrc16_start);
        }

        this.bMsg = new Message(byteBuffer, this.wLen);
        this.wCrc16_end = byteBuffer.getShort();

        byte[] footer = ByteBuffer.wrap(bytes, FOOTER_OFFSET, FOOTER_OFFSET + wLen - 1).array();
        short crc_end = CRC16Util.crc(footer);
        if (this.wCrc16_end != crc_end) {
            throw new PacketException("Wrong CRC16 end, must be "
                    + crc_end + ", have " + this.wCrc16_end);
        }
    }

    public PacketInfo getPacketInfo() {
        return decryptedMessage(this.bMsg);
    }

    private PacketInfo decryptedMessage(Message message) {

    }
}
