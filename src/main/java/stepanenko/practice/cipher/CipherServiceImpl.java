package stepanenko.practice.cipher;

import javax.crypto.Cipher;
import stepanenko.practice.util.KeyUtil;

public class CipherServiceImpl implements CipherService {

    public CipherServiceImpl() {
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance(KeyUtil.CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, KeyUtil.getKey());
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Can't encrypt the message", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance(KeyUtil.CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, KeyUtil.getKey());
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Can't decrypt the message", e);
        }
    }
}
