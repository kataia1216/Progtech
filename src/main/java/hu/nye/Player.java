package hu.nye;

public class Player {
    private String nev;
    private char szin;

    public Player(String nev, char szin) {
        this.nev = nev;
        this.szin = szin;
    }

    //Getterek
    public String getNev() {
        return nev;
    }

    //Getterek
    public char getSzin() {
        return szin;
    }

    //Setterek
    public void setSzin(char szin) {
        this.szin = szin;
    }

    //Setterek
    public void setNev(String nev) {
        this.nev = nev;
    }

    @Override
    public String toString() {
        return "Player{name='" + nev + "', color=" + szin + "}";
    }
}
