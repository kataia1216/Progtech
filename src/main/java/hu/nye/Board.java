package hu.nye;

import java.sql.SQLOutput;

public class Board {

    private final int sorok;
    private final int oszlopok;
    private final String[][] tabla;

    /**
     * konstruktor
     */
    public Board(int sorok, int oszlopok) {

        //ha a táblajáték mérete nem megfelelő dobjon hibát.
        if(sorok < 4 || oszlopok < 4 || sorok > 12 || oszlopok > 12)
            throw new IllegalArgumentException("Nem megfelelő a tábla paraméterei.");

        this.sorok = sorok;
        this.oszlopok = oszlopok;
        this.tabla = new String[sorok][oszlopok]; //tabla inicial. 2dimenzios tomben
    }

    public void megjelenit() {

        for (int i = 0; i < oszlopok; i++) {
            System.out.print(i+1 + " "); // Oszlop számok alul

        }
        System.out.println("");
        for (int i = sorok - 1; i >= 0; i--) {
            for (int j = 0; j < oszlopok; j++) {
                System.out.print((tabla[i][j] == null ? "." : tabla[i][j]) + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    public boolean korongelhelyez(int oszlop, String korong) {
        if (oszlop < 0 || oszlop >= oszlopok) {
            throw new IllegalArgumentException("Érvénytelen oszlop!");
        }
        for (int i = 0; i < sorok; i++) {
            if (tabla[i][oszlop] == null) { // Végigmegy az adott oszlop összes sorain, s ha talál egy üres cellát oda korongot helyez.
                tabla[i][oszlop] = korong;
                return true;
            }
        }
        return false; // Az oszlop tele van
    }

    //ellenőrzés
    public boolean ellenorzes() {
        for (int sor = 0; sor < sorok; sor++) {
            for (int oszlop = 0; oszlop < oszlopok; oszlop++) {
                String kezdoCella = tabla[sor][oszlop];
                if (kezdoCella != null && !kezdoCella.equals(".")) {
                    // Négy irány ellenőrzése
                    if (vizsgalat(sor, oszlop, 1, 0, kezdoCella) || // Vízszintes
                            vizsgalat(sor, oszlop, 0, 1, kezdoCella) || // Függőleges
                            vizsgalat(sor, oszlop, 1, 1, kezdoCella) || // Átlós (jobbra lefelé)
                            vizsgalat(sor, oszlop, 1, -1, kezdoCella))  // Átlós (balra lefelé)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean vizsgalat(int sor, int oszlop, int dx, int dy, String kezdoCella) {
        for (int i = 1; i < 4; i++) {
            int ujSor = sor + i * dx;
            int ujOszlop = oszlop + i * dy;

            // Ellenőrizni kell, hogy az új pozíció a táblán belül van-e
            if (ujSor < 0 || ujSor >= sorok || ujOszlop < 0 || ujOszlop >= oszlopok) {
                return false;
            }

            // Ellenőrizzük, hogy a cella nem null, és azonos a kezdoCella értékével
            if (tabla[ujSor][ujOszlop] == null || !tabla[ujSor][ujOszlop].equals(kezdoCella)) {
                return false;
            }
        }
        return true;
    }

    //Getterek a lekérdezéshez

    public int getSorok() {
        return sorok;
    }

    public int getOszlopok() {
        return oszlopok;
    }

    public String[][] getTabla() {
        return tabla;
    }


}
