package stepanenko.homework2.service;

import stepanenko.homework2.model.Command;
import stepanenko.homework2.model.Store;
import stepanenko.practice1.exception.StoreException;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.processing.EncryptorImpl;
import stepanenko.practice1.util.ByteConverter;

public class ProcessorImpl implements Processor {
    public static ProcessorImpl getInstance() {
        return new ProcessorImpl();
    }

    @Override
    public void process(Message message) {
        if (message == null) {
            throw new RuntimeException("Message is null");
        }
        Message answer;
        try {
            Object[] messageContent =
                    (Object[]) ByteConverter.convertBytesToObject(message.getMessage());
            switch (message.getCommand()) {
                case SHOW_PRODUCT_AMOUNT -> Store.getProductAmount((int) messageContent[0]);
                case SET_PRODUCT_AMOUNT -> Store.updateProductAmount((int) messageContent[0],
                        (int) messageContent[1]);
                case ADD_PRODUCT_GROUP -> Store.addProductGroup((String) messageContent[0]);
                case ADD_PRODUCT_TO_GROUP -> Store.addProductToGroup((int) messageContent[0],
                        (String) messageContent[1]);
                case SET_PRODUCT_PRICE -> Store.setProductPrice((int) messageContent[0],
                        (int) messageContent[1]);
                default -> answer = new Message(Command.ERROR, message.getUserId(),
                        ByteConverter.convertObjectToBytes("Invalid command number was chosen"));
            }
            answer = new Message(Command.OK, message.getUserId(),
                    ByteConverter.convertObjectToBytes("OK"));
        } catch (ClassCastException e) {
            answer = new Message(Command.ERROR, message.getUserId(),
                    ByteConverter.convertObjectToBytes("Invalid input parameters were set"));
        } catch (StoreException e) {
            answer = new Message(Command.ERROR, message.getUserId(),
                    ByteConverter.convertObjectToBytes("Storage operation "
                            + "can't be processed: " + e.getMessage()));
        }
        EncryptorImpl.getInstance().encryptMessage(answer);
    }
}
