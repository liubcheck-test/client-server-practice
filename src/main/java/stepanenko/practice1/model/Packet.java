package stepanenko.practice1.model;

import java.nio.ByteBuffer;
import java.util.Objects;

import stepanenko.practice1.exception.CRC16Exception;
import stepanenko.practice1.exception.MagicByteException;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice1.util.CRC16Util;
import stepanenko.practice1.util.CipherUtil;

/**
 * @author Liubomyr Stepanenko
 */
public class Packet {
    public static final byte B_MAGIC = 0x13;
    public static final int HEADER_LENGTH = 14;
    public static long PACKET_ID = 0;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16_start, wCrc16_end;
    private Message bMsg;

    public Packet(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        if (byteBuffer.get() != B_MAGIC) {
            throw new MagicByteException("Wrong packet start, must be "
                    + Integer.toHexString(B_MAGIC) + ", have "
                    + Integer.toHexString(byteBuffer.get()));
        }
        this.bSrc = byteBuffer.get();
        this.bPktId = byteBuffer.getLong();
        this.wLen = byteBuffer.getInt();
        this.wCrc16_start = byteBuffer.getShort();
        this.bMsg = decryptedMessage(byteBuffer);
        this.wCrc16_end = byteBuffer.getShort();
        checkHeader();
        checkFooter();
    }

    private Message decryptedMessage(ByteBuffer byteBuffer) {
        try {
            byte[] bytes = getBytes(byteBuffer, this.wLen);
            ByteBuffer decryptedByteBuffer =
                    ByteBuffer.wrap(CipherUtil.getDecryptCipher().doFinal(bytes));
            return new Message(decryptedByteBuffer.getInt(),
                    decryptedByteBuffer.getInt(),
                    getBytes(decryptedByteBuffer, decryptedByteBuffer.remaining()));
        } catch (Exception e) {
            throw new RuntimeException("Can't decrypt the message", e);
        }
    }

    private byte[] getBytes(ByteBuffer byteBuffer, int length) {
        byte[] bytes = new byte[length];
        byteBuffer.get(bytes);
        return bytes;
    }

    private void checkHeader() {
        short crc_start = CRC16Util.crc(getHeader());
        if (this.wCrc16_start != crc_start) {
            throw new CRC16Exception("Wrong CRC16 start, must be "
                    + crc_start + ", have " + this.wCrc16_start);
        }
    }

    private void checkFooter() {
        short crc_end = CRC16Util.crc(new EncryptorImpl().encryptMessage(this.bMsg));
        if (this.wCrc16_end != crc_end) {
            throw new CRC16Exception("Wrong CRC16 end, must be "
                    + crc_end + ", have " + this.wCrc16_end);
        }
    }

    private byte[] getHeader() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_LENGTH);
        byteBuffer.put(B_MAGIC)
                .put(this.bSrc)
                .putLong(this.bPktId)
                .putInt(this.wLen);
        byte[] headerBytes = new byte[HEADER_LENGTH];
        byteBuffer.rewind().get(headerBytes);
        return headerBytes;
    }

    public byte getbSrc() {
        return bSrc;
    }

    public void setbSrc(byte bSrc) {
        this.bSrc = bSrc;
    }

    public long getbPktId() {
        return bPktId;
    }

    public void setbPktId(long bPktId) {
        this.bPktId = bPktId;
    }

    public int getwLen() {
        return wLen;
    }

    public void setwLen(int wLen) {
        this.wLen = wLen;
    }

    public short getwCrc16_start() {
        return wCrc16_start;
    }

    public void setwCrc16_start(short wCrc16_start) {
        this.wCrc16_start = wCrc16_start;
    }

    public short getwCrc16_end() {
        return wCrc16_end;
    }

    public void setwCrc16_end(short wCrc16_end) {
        this.wCrc16_end = wCrc16_end;
    }

    public Message getbMsg() {
        return bMsg;
    }

    public void setbMsg(Message bMsg) {
        this.bMsg = bMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return bSrc == packet.bSrc && bPktId == packet.bPktId
                && wLen == packet.wLen && wCrc16_start == packet.wCrc16_start
                && wCrc16_end == packet.wCrc16_end && bMsg.equals(packet.bMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bSrc, bPktId, wLen, wCrc16_start, wCrc16_end, bMsg);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Packet{");
        sb.append("bSrc=").append(bSrc);
        sb.append(", bPktId=").append(bPktId);
        sb.append(", wLen=").append(wLen);
        sb.append(", wCrc16_start=").append(wCrc16_start);
        sb.append(", wCrc16_end=").append(wCrc16_end);
        sb.append(", bMsg=").append(bMsg);
        sb.append('}');
        return sb.toString();
    }
}
