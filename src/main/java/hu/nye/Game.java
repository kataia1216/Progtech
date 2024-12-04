package hu.nye;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Board tabla;
    private final FileManager filekezeles;
    private boolean kovjatekos = true; //Játékosok közötti váltás, kezdetben a felhasználó kezd.
    private Player jatekos;
    private Player szamitogep;
    private final dataBase adatbazisKezelo;

    public Game(int sorok, int oszlopok) {
        this.tabla = new Board(sorok, oszlopok);
        this.filekezeles = new FileManager();
        this.adatbazisKezelo = new dataBase();
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
        System.out.println("Szeretnéd betölteni a legutolsó játékállást? Igen (i) Nem (n)");
        Scanner be = new Scanner(System.in);
        String beolvas = be.nextLine();
        boolean amigNemJoBillentyzetettUtLe  = true;

        while(amigNemJoBillentyzetettUtLe){
            if(!beolvas.equals("i") && !beolvas.equals("n"))
            {
                System.out.println("Nem jó billentyűt ütöttél le! Próbálj újra!");
                beolvas = be.nextLine();
            }
            else if (beolvas.equals("i"))
            {
                filekezeles.betolt(tabla, "allapot.xml");
                amigNemJoBillentyzetettUtLe = false;
            }
            else
            {
                System.out.println("Üres pályáról indul a játék. Te kezdel!");
                amigNemJoBillentyzetettUtLe = false;
            }
        }

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

            // Tábla aktuális mentése minden kör után.
            filekezeles.mentes(tabla, "allapot.xml");

            // Van-e nyertes
            if (tabla.ellenorzes()) {
                tabla.megjelenit();
                if (kovjatekos) {
                    System.out.println("Gratulálunk, " + jatekos.getNev() + ", nyertél!");
                    adatbazisKezelo.gyozelemRogzites(jatekos.getNev());

                } else {
                    System.out.println("Sajnos vesztettél. A számítógép nyert.");
                    adatbazisKezelo.gyozelemRogzites(szamitogep.getNev());
                }
                jatekFolyamatban = false; // Kilépés a ciklusból
            }
            else if(tabla.tablaTeli())
            {
                tabla.megjelenit();
                System.out.println("A játék döntetlen eredménnyel zárult, mert nem maradt több üres cella.");
                jatekFolyamatban = false;
            }

            // Játékos és gép közötti váltás
            kovjatekos = !kovjatekos;
        }
           /*
        // Játék végén mentés lehetősége
        Scanner scanner = new Scanner(System.in);
        System.out.print("Szeretnéd menteni a játékállást? (i/n): ");
        String valasz = scanner.nextLine();
        if (valasz.equals("i")) {
            filekezeles.mentes(tabla, "allapot.xml");
        } else {
            System.out.println("Játékállás nem került mentésre.");
        }
        */

        System.out.println("A játék véget ért. Köszönöm, hogy játszottál!");
        adatbazisKezelo.highScoreMegjelenit();
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

        //ha ures a bemenet
        if (input.isEmpty()) {
            System.out.println("A bemenet nem lehet üres! Kérlek, próbáld újra.");
            return false;
        }

        //ha a bemeneti karakter nagyobb mint 1, vagy a kezdő karakter kisebb, mint 'A' karakter, vagy nagyobb mint
        // 'A' karakter + tábla oszlopainak száma akkor hiba.

        if (input.length() != 1 || input.charAt(0) < 'A' || input.charAt(0) >= 'A' + tabla.getOszlopok()) {
            System.out.println("Érvénytelen oszlop! Kérlek, válassz egy érvényes betűt.");
            return false;
        }

        int oszlop = input.charAt(0) - 'A'; // pl: B oszlop ASCII kódja:  66
        if (!tabla.korongelhelyez(oszlop, String.valueOf(jatekos.getSzin()))) {
            System.out.println("Ez az oszlop már tele van! Próbálj másikat!");
            return false;
        }

        Move move = new Move(jatekos, oszlop); // Lépés rögzítése
        System.out.println("Lépés: " + move);
        return true;
    }

}
