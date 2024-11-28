package hu.nye;

public class Move {

    private Player felhasznalo;
    private int oszlopa;

    // Konstruktor
    public Move(Player felhasznalo, int oszlopa) {
        this.felhasznalo = felhasznalo;
        this.oszlopa = oszlopa;}

    //Setterek
    public void setFelhasznalo(Player felhasznalo) {
        this.felhasznalo = felhasznalo;
    }

    //Setterek
    public void setOszlopa(int oszlopa) {
        this.oszlopa = oszlopa;
    }

    //Getterek
    public Player getFelhasznalo() {
        return felhasznalo;
    }
    //Getterek
    public int getOszlopa() {
        return oszlopa;
    }

    @Override
    public String toString() {
        return "Move{player=" + felhasznalo + ", column=" + oszlopa + "}";
    }
}
