package stepanenko.homework2;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stepanenko.homework2.model.Store;
import stepanenko.homework2.service.Receiver;
import stepanenko.homework2.service.ReceiverImpl;
import java.util.ArrayList;
import java.util.List;

public class StoreThreadTester {
    private static final int FIRST_GROUP_ID = 1, THREAD_NUMBER = 10;
    private static List<Thread> storeThreads;
    private Receiver receiver;

    @BeforeClass
    public static void init() {
        Store.addProductGroup("goods");
        Store.addProductGroup("chemicals");
        Store.addProductToGroup(FIRST_GROUP_ID, "shirt");
        storeThreads = new ArrayList<>();
    }

    @Test
    public void add_productsToTheGroup_Ok() {
        int initialProductNumber = getGroupSize(FIRST_GROUP_ID);
        startThreads();
        stopThreads();
        int finalProductNumber = getGroupSize(FIRST_GROUP_ID);
        Assert.assertEquals(initialProductNumber + THREAD_NUMBER, finalProductNumber);
    }

    private int getGroupSize(int groupId) {
        return Store.getGroupsProducts().get(groupId).size();
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
