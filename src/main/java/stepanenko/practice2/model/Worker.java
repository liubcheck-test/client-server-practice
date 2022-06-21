package stepanenko.practice2.model;

public class Worker extends Thread {
    private final int id;
    private final Data data;

    public Worker(int id, Data d) {
        this.id = id;
        this.data = d;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 5; i++) {
            synchronized (data) {
                try {
                    while (id != data.getState()) {
                        data.wait();
                    }
                    switch (id) {
                        case 1 -> data.tac();
                        case 2 -> data.toe();
                        case 3 -> data.tic();
                    }
                    data.notify();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Can't solve the exception", e);
                }
            }
        }
    }
}
