package model;

import java.nio.ByteBuffer;

public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;

    public Message(ByteBuffer byteBuffer, int wLen) {
        this.cType = byteBuffer.getInt();
        this.bUserId = byteBuffer.getInt();
        this.message = new byte[wLen - Integer.BYTES * 2];
        byteBuffer.get(this.message, Integer.BYTES * 2, wLen);
    }
}
