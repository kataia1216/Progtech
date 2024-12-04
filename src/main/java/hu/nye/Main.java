package hu.nye;

public class Main {

    public static void main( String[] args )
    {
        Game game = new Game(6, 7); // 6x7-es tábla
        dataBase db = new dataBase();
        db.AdatbazisKezelo();
        game.start();
    }
}
