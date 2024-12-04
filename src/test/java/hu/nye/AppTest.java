package hu.nye;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void tesztTablaInicializalasa() {
        Board tabla = new Board(6, 7);
        assertEquals(6, tabla.getSorok());
        assertEquals(7, tabla.getOszlopok());
        assertNotNull(tabla.getTabla());
    }

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

    @Test
    void testKorongElhelyez_SikeresLepes() {
        Board tabla = new Board(6, 7);
        boolean eredmeny = tabla.korongelhelyez(3, "Y");
        assertTrue(eredmeny, "A lépésnek sikeresnek kell lennie.");
        assertEquals("Y", tabla.getTabla()[0][3], "A korongnak a megfelelő helyre kellett kerülnie.");
    }

    @Test
    void tesztGyoztesVizsgalataFuggolegesen() {
        Board tabla = new Board(6, 7);
        tabla.korongelhelyez(0, "Y");
        tabla.korongelhelyez(0, "Y");
        tabla.korongelhelyez(0, "Y");
        tabla.korongelhelyez(0, "Y");
        assertTrue(tabla.ellenorzes()); // Négy azonos korong van függőlegesen
    }

    @Test
    void tesztGyoztesVizsgalataVizszintesen() {
        Board tabla = new Board(6, 7);
        tabla.korongelhelyez(0, "Y");
        tabla.korongelhelyez(1, "Y");
        tabla.korongelhelyez(2, "Y");
        tabla.korongelhelyez(3, "Y");
        assertTrue(tabla.ellenorzes()); // Négy azonos korong van vízszintesen
    }

    @Test
    void tesztGyoztesVizsgalataAtlosan() {
        Board tabla = new Board(6, 7);
        tabla.korongelhelyez(3, "Y");
        tabla.korongelhelyez(2, "Y");
        tabla.korongelhelyez(1, "Y");
        tabla.korongelhelyez(0, "Y");
        assertTrue(tabla.ellenorzes()); // Négy azonos korong van az ellentétes átlón
    }

    @Test
    void tesztMentes() throws IOException {
        Board board = new Board(6, 7);
        FileManager fileManager = new FileManager();

        // Fájl létrehozása
        File file = File.createTempFile("test", ".txt");
        file.deleteOnExit();

        // Mentés és betöltés
        fileManager.mentes(board, file.getAbsolutePath());
        Board tabla = new Board(6, 7);
        fileManager.betolt(tabla, file.getAbsolutePath());

        // Ellenőrzés
        assertArrayEquals(board.getTabla(), tabla.getTabla());
    }

}
