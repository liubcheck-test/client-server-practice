package stepanenko.practice.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Random;

/**
 * @author Liubomyr Stepanenko
 */
public class KeyUtil {
    public static final String CIPHER_TRANSFORMATION = "AES";
    private static final int KEY_SIZE = 128;
    private static final Key key = initRandomKey();

    public static Key getKey() {
        return key;
    }

    private static Key initRandomKey() {
        byte[] randomKeyBytes = new byte[KEY_SIZE / 8];
        Random random = new Random();
        random.nextBytes(randomKeyBytes);
        return new SecretKeySpec(randomKeyBytes, CIPHER_TRANSFORMATION);
    }
}
