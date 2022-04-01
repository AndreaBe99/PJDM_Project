package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Acquisizione {
	
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final int id;
	private String utente, account;
	
	private double alcol, peso;
	private String data;




	public Acquisizione(double alcol, double peso, String data, String utente, String account) {
		super();
		this.alcol = alcol;
		this.peso = peso;
		this.data = data;
		this.utente = utente;
		this.account = account;
		id = count.incrementAndGet(); 
	}


	@Override
	public String toString() {
		return "Acquisizione [id=" + id + ", utente=" + utente + ", account=" + account + ", alcol=" + alcol + ", data="
				+ data + "]";
	}
	
	

	public double getPeso() {
		return peso;
	}


	public void setPeso(double peso) {
		this.peso = peso;
	}


	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public double getAlcol() {
		return alcol;
	}
	public void setAlcol(double alcol) {
		this.alcol = alcol;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}


}
