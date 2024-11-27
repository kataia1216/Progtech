package hu.nye;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Egyszerű JUnit 5 tesztosztály.
 */
public class AppTest {

    /**
     * Egy teszt, amely ellenőrzi, hogy a feltétel igaz-e.
     */
    @Test
    public void testApp() {
        assertTrue(true, "A feltétel igaz.");
    }

    @Test
    void testPlayerCreation() {
        Player player = new Player("Teszt", 'Y');
        assertEquals("Teszt", player.getNev());
        assertEquals('Y', player.getSzin());
    }

    @Test
    void testMoveCreation() {
        Player player = new Player("Teszt", 'R');
        Move move = new Move(player, 3);
        assertEquals(player, move.getFelhasznalo());
        assertEquals(3, move.getOszlopa());
    }

}
