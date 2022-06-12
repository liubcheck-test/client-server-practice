package stepanenko.practice.processing;

import stepanenko.practice.cipher.CipherService;
import stepanenko.practice.model.Message;
import stepanenko.practice.model.Packet;

/**
 * @author Liubomyr Stepanenko
 */
public class PacketDecryptor {
    private Packet packet;
    private CipherService cipherService;

    public PacketDecryptor(Packet packet, CipherService cipherService) {
        if (packet == null || cipherService == null) {
            throw new IllegalArgumentException("Some argument is null");
        }
        this.packet = packet;
        this.cipherService = cipherService;
    }


    public byte[] decryptedMessage() {
        return cipherService.decrypt(this.packet.getbMsg().getMessage());
    }
}
