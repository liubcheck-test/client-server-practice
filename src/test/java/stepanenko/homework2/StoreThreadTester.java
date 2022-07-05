package stepanenko.homework2;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stepanenko.homework2.model.Store;
import stepanenko.homework2.service.Receiver;
import stepanenko.homework2.service.ReceiverImpl;

public class StoreThreadTester {
    private static final int INITIAL_GROUP_NUMBER = 2, THREAD_NUMBER = 10;
    private static List<Thread> storeThreads;
    private Receiver receiver;

    @BeforeAll
    public static void init() {
        Store.addProductGroup("goods");
        Store.addProductGroup("chemicals");
        storeThreads = new ArrayList<>();
    }

    @Test
    public void add_productGroups_Ok() {
        startThreads();
        stopThreads();
        int finalGroupNumber = Store.getGroups().size();
        Assertions.assertEquals(INITIAL_GROUP_NUMBER + THREAD_NUMBER, finalGroupNumber);
    }

    private void startThreads() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            receiver = new ReceiverImpl();
            storeThreads.add(new Thread(() -> receiver.receiveMessage()));
            storeThreads.get(i).start();
        }
    }

    private void stopThreads() {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            try {
                storeThreads.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
