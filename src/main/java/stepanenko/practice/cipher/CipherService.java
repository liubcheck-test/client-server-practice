package stepanenko.practice.cipher;

public interface CipherService {
    byte[] encrypt(byte[] bytes);
    byte[] decrypt(byte[] bytes);
}
