package be.vdab;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
Vuistregel: 
Lees records uit de 1 (één < multipliciteit) kant van een relatie via joins in je SQL statements.
Voorbeeld: 
Maak een overzicht met de namen van de rode planten (uit de table planten) 
en naast iedere naam de bijbehorende leveranciersnaam (uit de gerelateerde table leveranciers).
=> Je leest dus data uit de n kant van de relatie (planten) en je hebt de bijbehorende data nodig uit de 1 kant van de relatie (leveranciers).
Verkeerde (want trage) oplossing:
> eerst enkel de planten (nog niet de leveranciers) lezen met een SQL select statement;
> daarna over deze gelezen planten itereren en per plant met een SQL select statement de bijbehorende leverancier lezen.
Reden:
Door de overvloed van SQL statements (en de resultaten van die SQL statments) die je over het intranet verstuurt,
wordt dit een trage applicatie.
*/
public class Vb12_2a_Slecht {
    private static final String URL = "jdbc:mysql://localhost/tuincentrum?useSSL=false";
    private static final String USER = "cursist";
    private static final String PASSWORD = "cursist";
    private static final String SELECT_RODE_PLANTEN = 
            "select naam, leverancierid from planten where kleur = 'rood'";     // Leest één keer enkel de rode planten
    private static final String SELECT_LEVERANCIER = 
            "select naam from leveranciers where id = ?";                       // Leest per plant de bijbehorende leverancier
    public static void main(String[] args) {
        // Opent Connection, Statement en PreparedStatement
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statementPlanten = connection.createStatement();
                PreparedStatement statementLeverancier = connection.prepareStatement(SELECT_LEVERANCIER)) {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            // Opent ResultSet van het statement
            try (ResultSet resultSetPlanten = statementPlanten.executeQuery(SELECT_RODE_PLANTEN)) {
                // Itereert over de rijen in de resultSetPlanten
                while (resultSetPlanten.next()) {
                    System.out.print(resultSetPlanten.getString("naam"));
                    System.out.print(' ');
                    statementLeverancier.setLong(1, resultSetPlanten.getLong("leverancierid"));
                    // Opent ResultSet van het PreparedStatement
                    try (ResultSet resultSetLeverancier = statementLeverancier.executeQuery()) {
                        System.out.println(resultSetLeverancier.next() ? resultSetLeverancier.getString("naam") : "leverancier niet gevonden");
                    }
                }
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
