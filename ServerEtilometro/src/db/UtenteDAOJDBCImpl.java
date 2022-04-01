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
import entity.Utente;


public class UtenteDAOJDBCImpl implements UtenteDAO{

	private Connection conn;

	public UtenteDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		
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
	public Utente loadUtente(String utente, String account) {

		String query = "SELECT * FROM utente WHERE nome = \"" + utente + "\" AND account = \"" + account + "\"";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			Utente res = null;

			while (rset.next()) {
				String nome = rset.getString(1);
				String sesso = rset.getString(2);
				double peso = rset.getDouble(3);
				String newAccount = rset.getString(4);

				res = new Utente(nome, sesso, peso, newAccount);

				break;
			}

			rset.close();
			stmt.close();

			return res;

		} catch (SQLException e) {
			e.printStackTrace();

			return null;
		}

	}
	
	
	
	@Override
	public int insertUtente(Utente utente) {
		String SQL = "INSERT INTO utente(nome,sesso,peso,account) " + "VALUES(?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, utente.getNome());
			pstmt.setString(2, utente.getSesso());
			pstmt.setDouble(3, utente.getPeso());
			pstmt.setString(4, utente.getAccount());

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
	public ArrayList<Utente> loadTuttiUtenti(String account) {
		String query = "SELECT * FROM utente WHERE account= \"" + account + "\"";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			ArrayList<Utente> res = new ArrayList<>();

			while (rset.next()) {
				String nome = rset.getString(1);
				String sesso = rset.getString(2);
				double peso = rset.getDouble(3);
				

				Utente u = new Utente(nome, sesso, peso, account);
				res.add(u);
			}

			rset.close();
			stmt.close();

			return res;

		} catch (SQLException e) {

			e.printStackTrace();

			return null;
		}

	}

	@Override
	public boolean deleteUtente(String nome, String account) {

		
		String SQL1 = "DELETE FROM acquisizione WHERE utente_acquisizione = \"" + nome + "\" AND account_acquisizione = \"" + account + "\"";
		String SQL2 = "DELETE FROM utente WHERE nome = \"" + nome + "\" AND account = \"" + account + "\"";

		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL1);
			statement.execute(SQL2);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
	}
	
	@Override
	public boolean updateUtente(String nome, String account, double newPeso) {

		String SQL = "UPDATE utente SET peso = \"" + newPeso + "\" WHERE nome = \"" + nome + "\" AND account = \"" + account + "\"";

		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	
	}

}

