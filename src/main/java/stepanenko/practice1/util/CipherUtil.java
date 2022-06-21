package stepanenko.practice1.util;

import java.security.Key;
import javax.crypto.Cipher;

/**
 * @author Liubomyr Stepanenko
 */
public class CipherUtil {
    private static final Key key = KeyUtil.getKey();

    public static Cipher getEncryptCipher() {
        return initCipher(Cipher.ENCRYPT_MODE);
    }

    public static Cipher getDecryptCipher() {
        return initCipher(Cipher.DECRYPT_MODE);
    }

    private static Cipher initCipher(int mode) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("Can't init cipher", e);
        }
    }
}
