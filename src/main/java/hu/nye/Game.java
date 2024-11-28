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

        // Játék megkezdése
        while (true) {
            tabla.megjelenit();

            if (kovjatekos) {
                if (!jatekosLep()) {        //amíg a játékos nem rögzíti a lépését addig, nem vált át a következő játékosra.
                    continue;
                }
            } else {                        //a kovjatekos false akkor a felhasznalo mar lepett és a gép következik.
                pclep();
            }

            if (tabla.ellenorzes()) {       //leellenőrizem a táblát, hogy az adott körben nyert, e már vlki. Ha az ell.
                tabla.megjelenit();         //true akkor már valaki nyert, hiszen valakinek valahol kijött a 4 korong.
                                            // ha az ellenorzes true akkor az adott iterációban ahol a kovjatekos true ott a felhasznalo nyert.
                if (kovjatekos) {
                    System.out.println("Gratulálunk, " + jatekos.getNev() + ", nyertél!");
                } else {
                    System.out.println("Sajnos vesztettél. A számítógép nyert.");
                }
                break;
            }

            kovjatekos = !kovjatekos; //játékos - gép között váltás.
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
