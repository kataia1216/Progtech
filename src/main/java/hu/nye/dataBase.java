package hu.nye;

import java.sql.*;

public class dataBase {
    private static final String eleres = "jdbc:sqlite:highscores.db";

    public void AdatbazisKezelo() {
        try (Connection csatlakozas = DriverManager.getConnection(eleres)) {   //adatbáziskapcsolat létrehozáas az elérési utat figyelembe véve
            Statement stmt = csatlakozas.createStatement(); //Olyan objektumot hoz létre amely sql utasítást hoz lehet futtatni.
            String sql = """
                CREATE TABLE IF NOT EXISTS jatekosok (
                    nev TEXT PRIMARY KEY,
                    gyozelmek INTEGER DEFAULT 0
                )
                """;
            stmt.execute(sql); //lefuttatja az sql utasítást
        } catch (SQLException e) {
            System.out.println("Adatbázis hiba!!! " + e.getMessage());
        }
    }

    public void gyozelemRogzites(String jatekosNev) {
        String sql = """
            INSERT INTO jatekosok (nev, gyozelmek)
            VALUES (?, 1)
            ON CONFLICT(nev) DO UPDATE SET gyozelmek = gyozelmek + 1
            """;
        try (Connection csatlakozas = DriverManager.getConnection(eleres);
             PreparedStatement pstmt = csatlakozas.prepareStatement(sql)) {
            pstmt.setString(1, jatekosNev);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Hiba a győzelem rögzítése során: " + e.getMessage());
        }
    }

    public void highScoreMegjelenit() {
        String sql = "SELECT nev, gyozelmek FROM jatekosok ORDER BY gyozelmek DESC";
        try (Connection csatlakozas = DriverManager.getConnection(eleres);
             Statement stmt = csatlakozas.createStatement();
             ResultSet eredmeny= stmt.executeQuery(sql)) {

            System.out.println("High Score Táblázat:");
            System.out.println("---------------------");
            while (eredmeny.next()) {
                    System.out.println(eredmeny.getString("nev") + " - " + eredmeny.getInt("gyozelmek") + " győzelem");
            }
        } catch (SQLException e) {
            System.out.println("Hiba a High Score megjelenítése során: " + e.getMessage());
        }
    }
}
