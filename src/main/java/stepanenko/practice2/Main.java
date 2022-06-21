package stepanenko.practice2;

import stepanenko.practice2.model.Data;
import stepanenko.practice2.model.Worker;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Data d = new Data();

        Worker worker1 = new Worker(1, d);
        Worker worker2 = new Worker(2, d);
        Worker worker3 = new Worker(3, d);

        worker1.join();
        System.out.println("end of main...");
    }
}