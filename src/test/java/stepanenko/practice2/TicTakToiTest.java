package stepanenko.practice2;

import org.junit.Test;
import java.io.*;
import static org.junit.Assert.assertEquals;

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
        assertEquals(expected, out.toString());
    }
}