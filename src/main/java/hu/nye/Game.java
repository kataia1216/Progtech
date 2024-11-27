package hu.nye;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private final Board tabla;
    private boolean kovjatekos = true;
    private String nev;
    private Player jatekos;
    private Player szamitogep;
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    public Game(int sorok, int oszlopok) {
        this.tabla = new Board(sorok, oszlopok);
    }

    private void inicializalas() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Üdvözöllek a Connect4 játékban!");
        System.out.print("Kérlek, add meg a neved: ");
        String nev = scanner.nextLine();
        jatekos = new Player(nev, 'Y'); // Játékos sárga színnel fog kezdeni
        szamitogep = new Player("Számítógép", 'R'); // A számítógép piros színnel fog lépni.

        System.out.println("Szia, " + jatekos.getNev() + "! A sárga koronggal fogsz játszani.");
    }


    private void betoltAllapot(String fileNev) {
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


    private void mentesAllapot(String fileNev) {
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
    
    public void start() {
        inicializalas();

        // Játékállapot betöltése induláskor
        betoltAllapot("allapot.txt");

        // Játék megkezdése
        while (true) {
            tabla.megjelenit();

            if (kovjatekos) {
                if (!jatekosLep()) {
                    continue;
                }
            } else {
                pclep();
            }

            if (tabla.ellenorzes()) {
                tabla.megjelenit();

                if (kovjatekos) {
                    System.out.println("Gratulálunk, " + nev + ", nyertél!");
                } else {
                    System.out.println("Sajnos vesztettél. A számítógép nyert.");
                }
                break;
            }

            kovjatekos = !kovjatekos;
        }

        // Játék végén mentés lehetősége
        Scanner scanner = new Scanner(System.in);
        System.out.print("Szeretnéd menteni a játékállást? (i/n): ");
        String valasz = scanner.nextLine();
        if (valasz.equals("i")) {
            mentesAllapot("allapot.txt");
            System.out.println("Játékállás elmentve az 'allapot.txt' fájlba.");

        } else {
            System.out.println("Játékállás nem került mentésre.");
        }

        System.out.println("A játék véget ért. Köszönöm, hogy játszottál!");
    }

    private void pclep() {
        Random random = new Random();
        boolean sikeres = false;
        int oszlop = -1;
        while (!sikeres) {
            oszlop = random.nextInt(tabla.getOszlopok());
            sikeres = tabla.korongelhelyez(oszlop, String.valueOf(szamitogep.getSzin()));
        }
        Move move = new Move(szamitogep, oszlop);
        System.out.println("A számítógép lépése: " + move);
    }

    private boolean jatekosLep() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(jatekos.getNev() + ", válassz egy oszlopot (A-" + (char) ('A' + tabla.getOszlopok() - 1) + "): ");
        String input = scanner.nextLine().toUpperCase();

        if (input.length() != 1 || input.charAt(0) < 'A' || input.charAt(0) >= 'A' + tabla.getOszlopok()) {
            System.out.println("Érvénytelen oszlop! Kérlek, válassz egy érvényes betűt.");
            return false;
        }

        int oszlop = input.charAt(0) - 'A';

        if (!tabla.korongelhelyez(oszlop, String.valueOf(jatekos.getSzin()))) {
            System.out.println("Ez az oszlop már tele van! Próbálj másikat!");
            return false;
        }

        Move move = new Move(jatekos, oszlop); // Lépés rögzítése
        System.out.println("Lépés: " + move);
        return true;
    }


}
