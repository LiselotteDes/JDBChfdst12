package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
Vuistregel: Lees enkel records die je nodig hebt.
Voorbeeld van een slechte oplossing: Je wil enkel de leveranciers uit Wevelgem.
*/
public class Vb12_1a_Slecht {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_ALLE_LEVERANCIERS =
            "select id, naam, woonplaats from leveranciers";
    public static void main(String[] args) {
        // Connection en Statement openen
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // Probeer eens met PreparedStatement ipv Statement
                PreparedStatement statement = connection.prepareStatement(SELECT_ALLE_LEVERANCIERS)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            // ResultSet openen. 
            try (ResultSet resultSet = statement.executeQuery()) {      // Leest alle leveranciers uit de database.
                int aantalLeveranciers = 0;
                while (resultSet.next()) {
                    if ("Wevelgem".equals(resultSet.getString("woonplaats"))) {     // Filtert de juiste leveranciers in Java code.
                        ++aantalLeveranciers;
                        System.out.println(resultSet.getInt("id") + " " + 
                                resultSet.getString("naam"));
                    }
                }
                System.out.println(aantalLeveranciers + " leverancier(s)");
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
