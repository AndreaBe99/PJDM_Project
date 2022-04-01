package db;

import java.util.ArrayList;

import entity.Account;
import entity.Acquisizione;
import entity.Utente;



public interface AcquisizioneDAO {

	//public Acquisizione loadAcquisizione(String utente, String account);
	
	public ArrayList<Acquisizione> loadTutteAcquisizioni(String utente, String account);

	public int insertAcquisizione(Acquisizione acquisizione);

	//public boolean deleteAcquisizione(Utente utente);

	public void closeConnection();


}