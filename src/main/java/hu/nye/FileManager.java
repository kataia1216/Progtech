package hu.nye;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {
    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

    public void mentes(Board tabla, String fileNev) {
        try (PrintWriter writer = new PrintWriter(fileNev)) {
            for (int i = tabla.getSorok() - 1; i >= 0; i--) {
                for (int j = 0; j < tabla.getOszlopok(); j++) {
                    writer.print((tabla.getTabla()[i][j] == null ? "." : tabla.getTabla()[i][j]) + " ");
                }
                writer.println();
            }
            System.out.println("Játékállás sikeresen elmentve.");
            logger.info("Játékállás elmentve a '{}' fájlba..", fileNev);
        } catch (IOException e) {
            System.out.println("Hiba történt az állapot mentése során: " + e.getMessage());
            logger.error("Nem sikerült menteni a játékállást a '{}' fájlba: {}", fileNev, e.getMessage());
        }
    }

    public void betolt(Board tabla, String fileNev) {
        File file = new File(fileNev);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                for (int i = tabla.getSorok() - 1; i >= 0; i--) {
                    String[] sor = scanner.nextLine().split(" ");
                    for (int j = 0; j < tabla.getOszlopok(); j++) {
                        tabla.getTabla()[i][j] = sor[j].equals(".") ? null : sor[j];
                    }
                }
                System.out.println("Játékállás betöltve a fájlból. Te lépsz.");
            } catch (IOException e) {
                System.out.println("Hiba történt az állapot betöltése során: " + e.getMessage());
            }
        } else {
            System.out.println("Nem található mentett állapot, üres pályával indul a játék. Te kezdesz.");
        }
    }
}
