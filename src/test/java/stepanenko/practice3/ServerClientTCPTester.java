package stepanenko.practice3;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stepanenko.homework2.model.Command;
import stepanenko.homework2.model.Store;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.util.ByteConverter;
import stepanenko.practice3.tcp.StoreClientTCP;
import stepanenko.practice3.tcp.StoreServerTCP;

public class ServerClientTCPTester {
    private static StoreServerTCP server;
    private static final int PORT = 1337;
    private static final int MAX_THREAD_NUMBER = 3;
    private static int userId = 100;

    @BeforeAll
    public static void init() {
        Store.addProductGroup("goods");
        Store.addProductGroup("chemicals");
        server = new StoreServerTCP(PORT, MAX_THREAD_NUMBER);
    }

    @Test
    public void tcp_Ok() throws IOException, InterruptedException {
        Message message1 = initMessage(Command.ADD_PRODUCT_TO_GROUP, userId++,
                new Object[]{1, "chocolate"});
        Message message2 = initMessage(Command.ADD_PRODUCT_TO_GROUP, userId++,
                new Object[]{1, "water"});
        Message message3 = initMessage(Command.ADD_PRODUCT_TO_GROUP, userId++,
                new Object[]{1, "milk"});
        StoreClientTCP client1 = new StoreClientTCP(PORT, message1);
        StoreClientTCP client2 = new StoreClientTCP(PORT, message2);
        StoreClientTCP client3 = new StoreClientTCP(PORT, message3);
        new Thread(server).start();
        Thread.sleep(100);
        new Thread(client1).start();
        new Thread(client2).start();
        new Thread(client3).start();
        Thread.sleep(800);
        server.stop();
        Assertions.assertEquals(3, Store.getProducts().size());
    }

    private Message initMessage(Command command, int userId, Object[] values) {
        return new Message(command, userId, ByteConverter.convertObjectToBytes(values));
    }
}
