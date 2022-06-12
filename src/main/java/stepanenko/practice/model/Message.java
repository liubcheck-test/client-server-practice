package stepanenko.practice.model;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Liubomyr Stepanenko
 */
public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;

    public Message() {
    }

    public Message(int cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public Message(ByteBuffer byteBuffer, int wLen) {
        this.cType = byteBuffer.getInt();
        this.bUserId = byteBuffer.getInt();
        this.message = new byte[wLen - Integer.BYTES * 2];
        byteBuffer.get(this.message, Integer.BYTES * 2, wLen);
    }

    public int getCType() {
        return cType;
    }

    public void setCType(int cType) {
        this.cType = cType;
    }

    public int getUserId() {
        return bUserId;
    }

    public void setbUserId(int bUserId) {
        this.bUserId = bUserId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return cType == message1.cType && bUserId == message1.bUserId && Arrays.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cType, bUserId);
        result = 31 * result + Arrays.hashCode(message);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("cType=").append(cType);
        sb.append(", bUserId=").append(bUserId);
        sb.append(", message=").append(Arrays.toString(message));
        sb.append('}');
        return sb.toString();
    }
}
