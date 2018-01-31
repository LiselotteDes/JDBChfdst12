package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
Vuistregel: 
Lees records uit de 1 (één < multipliciteit) kant van een relatie via joins in je SQL statements.
Voorbeeld: 
Maak een overzicht met de namen van de rode planten (uit de table planten) 
en naast iedere naam de bijbehorende leveranciersnaam (uit de gerelateerde table leveranciers).
Goede (want performantere) oplossing:
> De rode planten én hun bijbehordende leveranciers met één select statement te lezen.
Reden:
- Performantie is beter: minder lezen op harddisk en minder netwerkverkeer.
- Code is korter
*/
public class Vb12_2b_Goed {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_RODE_PLANTEN = 
            "select p.naam as plantnaam, l.naam as leveranciersnaam " +
            "from planten p inner join leveranciers l " +
            "on p.leverancierid = l.id " +
            "where kleur = 'rood'";
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            try (ResultSet resultSet = statement.executeQuery(SELECT_RODE_PLANTEN)) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("plantnaam") + " " + resultSet.getString("leveranciersnaam"));
                }
            }
            connection.commit();        // !!!!!! VERGEET IK VAAK
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
