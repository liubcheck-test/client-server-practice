package stepanenko.practice2.model;

public class Data {
    private int state = 3;

    public int getState() {
        return state;
    }

    public void tic() {
        System.out.print("Tic-");
        state = 1;
    }

    public void tac() {
        System.out.print("Tac-");
        state = 2;
    }

    public void toe() {
        System.out.println("Toe");
        state = 3;
    }
}
