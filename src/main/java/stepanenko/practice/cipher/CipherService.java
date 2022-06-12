package stepanenko.practice.cipher;

/**
 * @author Liubomyr Stepanenko
 */
public interface CipherService {
    byte[] encrypt(byte[] bytes);
    byte[] decrypt(byte[] bytes);
}
