package tdt4140.gr1801.web.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


// Denne klassen produserer PreparedStatements, som gjoer at man kan bestemme hva slags spoerringer/kommandoer som kan gjoeres opp mot databasen.
// Dersom en spoerring skal kunne gjoeres, skal den ligge her. 
public final class QueryFactory {
	
	
	// Hente SHA-256-hashet passord fra databasen
	public static PreparedStatement getPassword(String PT_ID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = DBConnection.getDBConnection();		// Kobler seg til database
		PreparedStatement stmt = conn.prepareStatement("SELECT Passwrd FROM PT WHERE PT_ID = ?");  	//Gjoer klar en spoerring
		stmt.setString(1, PT_ID);	// Setter sporsmoltegn i Statementet til PT_ID. Er 1-indeksert
		return stmt;
	}

	// Hente informasjon om en PT
	public static PreparedStatement getPT(String PT_ID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = DBConnection.getDBConnection();		// Kobler seg til database
		PreparedStatement stmt = conn.prepareStatement("SELECT PT_ID, Navn, Email, Birthday, Phonenr FROM PT WHERE PT_ID = ?");  	//Gjoer klar en spoerring
		stmt.setString(1, PT_ID);		// Setter sporsmoltegn i Statementet til PT_ID. Er 1-indeksert
		return stmt;
	}
	
	
	
	// Hente informasjon om en klient
	public static PreparedStatement getClient(Integer ClientID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Klient WHERE ClientID = ?");  
		stmt.setInt(1, ClientID);
		return stmt;
	}
	
	
	// Prepared Statement for aa hente informasjonen om alle klientene til en PT
	public static PreparedStatement getAllClients(String PT_ID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Klient WHERE PT_ID = ?");  
		stmt.setString(1, PT_ID);
		return stmt;
	}
	
	// PreparedStatement for innsetting av PT i databasen
	public static PreparedStatement insertPT(String PT_ID, String Passwrd, String Navn, String Email, String Birthday, String Phonenr) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = DBConnection.getDBConnection();
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO PT(PT_ID, Passwrd, Navn, Email, Birthday, Phonenr) VALUES (?, ?, ?, ?, ?, ?)");  
		stmt.setString(1, PT_ID);
		stmt.setString(2, Passwrd);
		stmt.setString(3, Navn);
		stmt.setString(4, Email);
		stmt.setString(5, Birthday);
		stmt.setString(6, Phonenr);
		return stmt;
	}
	
	
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		PreparedStatement stmt = QueryFactory.insertPT("vildera","Vilde Arntzen", "vildera@stud.ntnu.no","90959409","5080db871da8cce0493c7929f4fdb87668518f075c157e4389b292976b6d48cf","19970603");
	}
	
	
	
	
	
	
	
	
	
	
}
