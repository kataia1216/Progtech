package hu.nye;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Board tabla;
    private boolean kovjatekos = true;
    private String nev;

    public Game(int sorok, int oszlopok) {
        this.tabla = new Board(sorok, oszlopok);
    }

    private void inicializalas() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Üdvözöllek a Connect4 játékban!");
        System.out.print("Kérlek, add meg a neved: ");
        nev = scanner.nextLine();
        System.out.println("Szia, " + nev + "! A sárga koronggal fogsz játszani.");
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
        } catch (IOException e) {
            System.out.println("Hiba történt az állapot mentése során: " + e.getMessage());
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

    private void pclep()
    {
        Random random = new Random();
        boolean sikeres = false;
        while(!sikeres)
        {
            //kiválaszt egy random oszlopot
            int oszlop = random.nextInt(tabla.getOszlopok());
            sikeres = tabla.korongelhelyez(oszlop, "R");
        }

    }

    private boolean jatekosLep() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(nev + ", válassz egy oszlopot (1-" + tabla.getOszlopok() + "): ");
        int oszlop;
        try {
            oszlop = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Kérlek, adj meg egy érvényes számot!");
            return false;
        }

        if (oszlop < 0 || oszlop >= tabla.getOszlopok()) {
            System.out.println("Érvénytelen oszlop! Kérlek, válassz 1 és " + tabla.getOszlopok() + " között.");
            return false;
        }

        if (!tabla.korongelhelyez(oszlop, "Y")) {
            System.out.println("Ez az oszlop már tele van! Próbálj másikat!");
            return false;
        }

        return true;
    }

}
