package hu.nye;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AppTest {

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
