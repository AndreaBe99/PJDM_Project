package db;

import java.util.ArrayList;

import entity.Account;
import entity.Utente;



public interface UtenteDAO {

	public Utente loadUtente(String utente, String account);
	
	public ArrayList<Utente> loadTuttiUtenti(String account);

	public int insertUtente(Utente utente);

	public boolean deleteUtente(String nome, String account);
	
	public boolean updateUtente(String nome, String account, double newPeso);

	public void closeConnection();


}