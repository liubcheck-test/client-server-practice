package stepanenko.practice1.processing;

import stepanenko.practice1.model.Packet;

/**
 * @author Liubomyr Stepanenko
 */
public class Decryptor {
    public Packet decryptPacket(byte[] bytes) {
        return new Packet(bytes);
    }
}
