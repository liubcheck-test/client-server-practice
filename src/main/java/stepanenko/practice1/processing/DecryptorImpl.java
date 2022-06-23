package stepanenko.practice1.processing;

import stepanenko.homework2.service.ProcessorImpl;
import stepanenko.practice1.model.Packet;

/**
 * @author Liubomyr Stepanenko
 */
public class DecryptorImpl implements Decryptor {
    private static DecryptorImpl decryptor;
    public static DecryptorImpl getInstance() {
        if (decryptor == null) {
            decryptor = new DecryptorImpl();
        }
        return decryptor;
    }

    public Packet decryptPacket(byte[] bytes) {
        return new Packet(bytes);
    }

    @Override
    public void decrypt(byte[] message) {
        Packet packet = decryptPacket(message);
        ProcessorImpl.getInstance().process(packet.getbMsg());
    }
}
