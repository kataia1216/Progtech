package hu.nye;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Board tabla;
    private final FileManager filekezeles; // Fájlkezelő osztály
    private boolean kovjatekos = true;
    private Player jatekos;
    private Player szamitogep;

    public Game(int sorok, int oszlopok) {
        this.tabla = new Board(sorok, oszlopok);
        this.filekezeles = new FileManager(); // Példányosítjuk az új osztályt
    }

    private void inicializalas() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Üdvözöllek a Connect4 játékban!");
        System.out.print("Kérlek, add meg a neved: ");
        String nev = scanner.nextLine();
        jatekos = new Player(nev, 'Y');
        szamitogep = new Player("Számítógép", 'R');
        System.out.println("Szia, " + jatekos.getNev() + "! A(z) \'" + jatekos.getSzin() + "\' koronggal fogsz játszani.");
    }

    public void start() {
        inicializalas();

        // Játékállapot betöltése induláskor
        filekezeles.betolt(tabla, "allapot.xml");

        // A játék folyamatban van
        boolean jatekFolyamatban = true;

        // Játék megkezdése
        while (jatekFolyamatban) {
            tabla.megjelenit();

            if (kovjatekos) {
                if (!jatekosLep()) { // Amíg a játékos nem rögzíti a lépését, nem vált át a következő játékosra
                    continue;
                }
            } else { // Ha a kovjatekos false, akkor a játékos lépett, és most a gép következik
                pclep();
            }

            // Mentsük a tábla aktuális állapotát minden kör után
            filekezeles.mentes(tabla, "allapot.xml");

            // Ellenőrizzük, hogy van-e nyertes
            if (tabla.ellenorzes()) {
                tabla.megjelenit();
                if (kovjatekos) {
                    System.out.println("Gratulálunk, " + jatekos.getNev() + ", nyertél!");
                } else {
                    System.out.println("Sajnos vesztettél. A számítógép nyert.");
                }
                jatekFolyamatban = false; // Kilépés a ciklusból
            }

            // Játékos és gép közötti váltás
            kovjatekos = !kovjatekos;
        }

        // Játék végén mentés lehetősége
        Scanner scanner = new Scanner(System.in);
        System.out.print("Szeretnéd menteni a játékállást? (i/n): ");
        String valasz = scanner.nextLine();
        if (valasz.equals("i")) {
            filekezeles.mentes(tabla, "allapot.xml");
        } else {
            System.out.println("Játékállás nem került mentésre.");
        }

        System.out.println("A játék véget ért. Köszönöm, hogy játszottál!");
    }


    private void pclep() {
        Random random = new Random();
        boolean sikeres = false;
        int oszlop = -1;
        while (!sikeres) { //addig fut amíg egy random helyre nem rögzíti a korongját a gép
            oszlop = random.nextInt(tabla.getOszlopok());
            sikeres = tabla.korongelhelyez(oszlop, String.valueOf(szamitogep.getSzin()));
        }
        System.out.println("A számítógép lépése: ");
    }

    private boolean jatekosLep() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(jatekos.getNev() + ", válassz egy oszlopot (A-" + (char) ('A' + tabla.getOszlopok() - 1) + "): ");
        String input = scanner.nextLine().toUpperCase();

        if (input.isEmpty()) {
            System.out.println("A bemenet nem lehet üres! Kérlek, próbáld újra.");
            return false;
        }

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
