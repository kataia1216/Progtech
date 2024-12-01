package hu.nye;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AppTest {

    @Test
    void testOszlopTele() {
        // létrehozom a táblát
        Board board = new Board(6, 7);

        // az első oszlopot feltöltöm Y színnel
        for (int i = 0; i < board.getSorok(); i++) {
            board.korongelhelyez(0, "Y");
        }
        // Most hogy az előző kód lefutott elvileg az adott oszlop teli van, ezért megpróbálok mégegy korongot elhelyezni
        boolean sikeres = board.korongelhelyez(0, "Y"); //tagadni

        // Mivel elvileg nem kéne elhelyezni a korogot ezért hibát kell dobjon.
        assertFalse(sikeres, "Az oszlop már tele van, nem helyezhető el új korong.");
    }
}
