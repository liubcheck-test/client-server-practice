package stepanenko.practice.processing;

import stepanenko.practice.cipher.CipherService;
import stepanenko.practice.model.Message;
import stepanenko.practice.model.Packet;

/**
 * @author Liubomyr Stepanenko
 */
public class PacketEncryptor {
    private Packet packet;
    private CipherService cipherService;

    public PacketEncryptor(Packet packet, CipherService cipherService) {
        if (packet == null || cipherService == null) {
            throw new IllegalArgumentException("Some argument is null");
        }
        this.packet = packet;
        this.cipherService = cipherService;
    }

    private byte[] encryptedMessage() {
        return cipherService.encrypt(packet.getbMsg().getMessage());
    }
}
