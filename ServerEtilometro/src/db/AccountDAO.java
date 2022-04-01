package db;

import java.util.ArrayList;

import entity.Account;



public interface AccountDAO {

	public Account loadAccount(String name, String password);
	

	public ArrayList<Account> loadTuttiAccount();

	public int insertAccount(Account account);

	public boolean deleteAccount(Account account);

	public void closeConnection();

}