package stepanenko.practice2;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Oleh Klepatskyi
 */
public class TicTakToiTest {
    final String expected = "Tic-Tac-Toe\r\n" +
            "Tic-Tac-Toe\r\n" +
            "Tic-Tac-Toe\r\n" +
            "Tic-Tac-Toe\r\n" +
            "Tic-Tac-Toe\r\n" +
            "end of main...\r\n";

    @Test
    public void some_test() throws InterruptedException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        Main.main(null);
        Assertions.assertEquals(expected, out.toString());
    }
}