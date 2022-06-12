package stepanenko.practice.model;

import java.nio.ByteBuffer;
import java.util.Objects;
import stepanenko.practice.exception.PacketException;
import stepanenko.practice.util.CRC16Util;

/**
 * @author Liubomyr Stepanenko
 */
public class Packet {
    public static final byte bMagic = 0x13;
    private static final int HEADER_LENGTH = 13, FOOTER_OFFSET = 16;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16_start, wCrc16_end;
    private Message bMsg;

    private byte[] header, footer;

    public Packet() {
        this.bMsg = new Message();
    }

    public Packet(byte bSrc, long bPktId, int wLen, short wCrc16_start,
                  Message bMsg, short wCrc16_end) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.wLen = wLen;
        this.wCrc16_start = wCrc16_start;
        this.bMsg = bMsg;
        this.wCrc16_end = wCrc16_end;
        createHeader();
        createFooter();
    }

    public Packet(byte[] bytes) {
        if (bytes[0] != bMagic) {
            throw new PacketException("Wrong packet start, must be "
                    + bMagic + "in hex format , have " + bytes[0]);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        this.bSrc = byteBuffer.get();
        this.bPktId = byteBuffer.getLong();
        this.wLen = byteBuffer.getInt();
        this.wCrc16_start = byteBuffer.getShort();

        this.header = ByteBuffer.wrap(bytes, 0, HEADER_LENGTH).array();
        checkHeader();

        this.bMsg = new Message(byteBuffer, this.wLen);
        this.wCrc16_end = byteBuffer.getShort();

        this.footer = ByteBuffer.wrap(bytes, FOOTER_OFFSET, wLen - 1).array();
        checkFooter();
    }

    private void createHeader() {
        this.header = ByteBuffer.allocate(HEADER_LENGTH)
                .put(bMagic)
                .put(this.bSrc)
                .putLong(this.bPktId)
                .putInt(this.wLen)
                .array();
        checkHeader();
    }

    private void createFooter() {
        this.footer = ByteBuffer.allocate(wLen - 1)
                .putInt(this.bMsg.getCType())
                .putInt(this.bMsg.getUserId())
                .put(this.bMsg.getMessage())
                .array();
        checkFooter();
    }

    private void checkHeader() {
        short crc_start = CRC16Util.crc(header);
        if (this.wCrc16_start != crc_start) {
            throw new PacketException("Wrong CRC16 start, must be "
                    + crc_start + ", have " + this.wCrc16_start);
        }
    }

    private void checkFooter() {
        short crc_end = CRC16Util.crc(footer);
        if (this.wCrc16_end != crc_end) {
            throw new PacketException("Wrong CRC16 end, must be "
                    + crc_end + ", have " + this.wCrc16_end);
        }
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

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getFooter() {
        return footer;
    }

    public void setFooter(byte[] footer) {
        this.footer = footer;
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
