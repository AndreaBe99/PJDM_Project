package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import db.AccountDAO;
import entity.Account;
import entity.Acquisizione;
import entity.Utente;


public class AcquisizioneDAOJDBCImpl implements AcquisizioneDAO{

	private Connection conn;

	public AcquisizioneDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException: " + e.getMessage());
		} 
	
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + "/" + dbName
							+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					userName, pwd);
		} catch (SQLException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}

	
	
	@Override
	public int insertAcquisizione(Acquisizione acquisizione) {
		String SQL = "INSERT INTO acquisizione(alcol,peso,data,utente_acquisizione,account_acquisizione) " + "VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setDouble(1, acquisizione.getAlcol());
			pstmt.setDouble(2, acquisizione.getPeso());
			pstmt.setString(3, acquisizione.getData());
			pstmt.setString(4, acquisizione.getUtente());
			pstmt.setString(5, acquisizione.getAccount());

			int affectedRows = pstmt.executeUpdate();

			return affectedRows;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Acquisizione> loadTutteAcquisizioni(String utente, String account) {
		String query = "SELECT * FROM acquisizione WHERE account_acquisizione= \"" + account + "\" AND utente_acquisizione = \"" + utente + "\"";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			ArrayList<Acquisizione> res = new ArrayList<>();

			while (rset.next()) {
				double alcol = rset.getDouble(2);
				String data = rset.getString(3);
				double peso = rset.getDouble(4);
				String newUtente = rset.getString(5);
				String newAccount = rset.getString(6);

				Acquisizione a = new Acquisizione(alcol, peso, data, newUtente, newAccount);
				res.add(a);
			}

			rset.close();
			stmt.close();

			return res;

		} catch (SQLException e) {

			e.printStackTrace();

			return null;
		}

	}

}

