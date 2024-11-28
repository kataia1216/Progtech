package hu.nye;

import java.sql.SQLOutput;

public class Board {

    private final int sorok;
    private final int oszlopok;
    private final String[][] tabla;

    /*
    Konstruktor
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

        // Oszlopok betűkkel
        for (int i = 0; i < oszlopok; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        // Táblázat tartalma
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
            throw new IllegalArgumentException("Érvénytelen oszlop!");                  //nem megfelelő oszlop választás
        }
        for (int i = 0; i < sorok; i++) {
            if (tabla[i][oszlop] == null) { // Végigmegy az adott oszlop összes sorain, s ha talál egy üres cellát oda korongot helyez.
                tabla[i][oszlop] = korong;
                return true;
            }
        }
        return false; // Az oszlop tele van
    }

    /*
      Az ellenörzés függvény végigmegy a játéktáblán és ha, az adott cella nem üres vagy null értéket vesz fel, akkor megvizsgálja a
      a négy irányt, és ha ott valamelyik irányban kijön az hogy true, tehát a 4 korong akkor igaz értékkel tér vissza
      Erre azért van szükség mert, amíg false értékkel tér vissza az azt jelenti hogy, nincs még győztes, tehát a játék fut.
      */

    public boolean ellenorzes() {
        for (int sor = 0; sor < sorok; sor++) {
            for (int oszlop = 0; oszlop < oszlopok; oszlop++) {
                String kezdoCella = tabla[sor][oszlop];                         //az éppen aktuális cella
                if (kezdoCella != null && !kezdoCella.equals(".")) {            //ha nem üres, tehát van benne egy korong
                    if (vizszintes(sor, oszlop, kezdoCella) ||                  //a 4 irány vizsgálata
                            fuggoleges(sor, oszlop, kezdoCella) ||
                            atlosJobbraLefele(sor, oszlop, kezdoCella) ||
                            atlosBalraLefele(sor, oszlop, kezdoCella)) {
                        return true;                                            //ha vlmelyik irány igaz akkor már vlki nyert.
                    }
                }
            }
        }
        return false;                                                           //nincs győztes, a játéknak nincs még vége, fut.
    }

    /*Ha minden egyes iterációban NEM false értéket kap, az azt jelenti, hogy a vizsgálat közben az akt. cella NEM null,
    az aktuális cella értéke NEM tér el a kezdő cella értékétől. és nem lép ki a tábla határain kívül*/

    private boolean vizszintes(int sor, int oszlop, String kezdoCella) {
        for (int i = 0; i < 4; i++) {                                                   //xxxx
            int aktualisOszlop = oszlop + i;                                            //....
            if (aktualisOszlop >= oszlopok || tabla[sor][aktualisOszlop] == null ||     //....
                    !tabla[sor][aktualisOszlop].equals(kezdoCella)) {                   //....
                return false;
            }
        }
        return true;
    }

    private boolean fuggoleges(int sor, int oszlop, String kezdoCella) {
        for (int i = 0; i < 4; i++) {                                                   //x...
            int aktualisSor = sor + i;                                                  //x...
            if (aktualisSor >= sorok || tabla[aktualisSor][oszlop] == null ||           //x...
                    !tabla[aktualisSor][oszlop].equals(kezdoCella)) {                   //x...
                return false;
            }
        }
        return true;
    }

    private boolean atlosJobbraLefele(int sor, int oszlop, String kezdoCella) {
        for (int i = 0; i < 4; i++) {
            int aktualisSor = sor + i;                                                   //x...
            int aktualisOszlop = oszlop + i;                                             //.x..
            if (aktualisSor >= sorok || aktualisOszlop >= oszlopok ||                    //..x.
                    tabla[aktualisSor][aktualisOszlop] == null ||                        //...x
                    !tabla[aktualisSor][aktualisOszlop].equals(kezdoCella)) {
                return false;
            }
        }
        return true;
    }

    private boolean atlosBalraLefele(int sor, int oszlop, String kezdoCella) {
        for (int i = 0; i < 4; i++) {
            int aktualisSor = sor + i;                                                   //...x
            int aktualisOszlop = oszlop - i;                                             //..x.
            if (aktualisSor >= sorok || aktualisOszlop < 0 ||                            //.x..
                    tabla[aktualisSor][aktualisOszlop] == null ||                        //x...
                    !tabla[aktualisSor][aktualisOszlop].equals(kezdoCella)) {
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
