package stepanenko.practice.processing;

import stepanenko.practice.model.Packet;

/**
 * @author Liubomyr Stepanenko
 */
public class Decryptor {
    public Packet decryptPacket(byte[] bytes) {
        return new Packet(bytes);
    }
}
