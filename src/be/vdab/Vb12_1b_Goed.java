package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
Vuistregel: Lees enkel records die je nodig hebt.
Voorbeeld: Je wil enkel de leveranciers uit Wevelgem.
Goede oplossing: Lees enkel de records die je nodig hebt via het where deel van het select statement.
Reden: De database stuurt bij deze oplossing enkel de leveranciers uit Wevelgem over het netwerk naar de applicatie.
De oplossing is snel omdat er minder bytes over het netwerk getransporteerd worden.
*/
public class Vb12_1b_Goed {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_LEVERANCIERS_UIT_WEVELGEM =      // Leest enkel de leveranciers uit Wevelgem uit de database. 
            "select id, naam, woonplaats from leveranciers where woonplaats='Wevelgem'";
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery(SELECT_LEVERANCIERS_UIT_WEVELGEM)){
                int aantalLeveranciers = 0;
                while (resultSet.next()) {      // Er moet in de java code niet meer gefilterd worden.
                    ++aantalLeveranciers;
                    System.out.println(resultSet.getInt("id") + " " + resultSet.getString("naam"));
                }
                System.out.println(aantalLeveranciers + " leveranciers");
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
