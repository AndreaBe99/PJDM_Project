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


public class AccountDAOJDBCImpl implements AccountDAO{

	private Connection conn;

	public AccountDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		
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
	public Account loadAccount(String name, String pass) {

		String query = "SELECT * FROM Account WHERE username = \"" + name + "\" AND password = \""+pass +"\"";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			Account res = null;

			while (rset.next()) {
				String username = rset.getString(1);
				String password = rset.getString(2);
				String email = rset.getString(3);

				res = new Account(username, password, email);

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
	public int insertAccount(Account account) {
		String SQL = "INSERT INTO Account(username,password,email) " + "VALUES(?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, account.getUsername());
			pstmt.setString(2, account.getPassword());
			pstmt.setString(3, account.getEmail());

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
	public ArrayList<Account> loadTuttiAccount() {
		String query = "SELECT * FROM Account ";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			ArrayList<Account> res = new ArrayList<>();

			while (rset.next()) {
				String username = rset.getString(1);
				String password = rset.getString(2);
				String email = rset.getString(3);

				Account m = new Account(username, password, email);
				res.add(m);
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
	public boolean deleteAccount(Account account) {

		String username = account.getUsername();

		String SQL = "DELETE FROM Account WHERE username='" + username + "'";

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

