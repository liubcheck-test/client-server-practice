package stepanenko.practice.model;

import java.nio.ByteBuffer;

public class PacketEncryptor {
    private static final byte bMagic = 0x13;
    private static final int REST_BYTES_QUANTITY = 18, HEADER_LENGTH = 13, FOOTER_OFFSET = 16;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16_start, wCrc16_end;
    private Message bMsg;

    public PacketEncryptor(byte bSrc, long bPtkId, int wlen, ) {
        ByteBuffer.allocate(message.getMessage().length + REST_BYTES_QUANTITY)
                .put(bMagic)
                .put();
        encryptMessage(message);
    }

    private void encryptMessage(Message message) {

    }
}
