package stepanenko.practice1.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Liubomyr Stepanenko
 */
public class KeyUtil {
    private static final String SECRET_KEY = "&mjlt2ls!sLVas.?";
    private static final Key key = initKey();

    public static Key getKey() {
        return key;
    }

    private static Key initKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }
}
