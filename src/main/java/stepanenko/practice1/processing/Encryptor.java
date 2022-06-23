package stepanenko.practice1.processing;

import stepanenko.practice1.model.Message;

public interface Encryptor {
    byte[] encrypt(Message message);
}
