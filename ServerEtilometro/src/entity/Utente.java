package entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Utente {
	
//	private static final AtomicInteger count = new AtomicInteger(0); 
//	private final int id;
	private String nome, account, sesso;
	
	private double peso;


	@Override
	public String toString() {
		return "Utente [nome=" + nome + ", sesso=" + sesso + ", peso=" + String.valueOf(peso) + ", account=" + account + "]";
	}

	public Utente(String nome, String sesso, double peso, String account) {
		super();
		this.nome = nome;
		this.sesso = sesso;
		this.peso = peso;
		this.account = account;
		//id = count.incrementAndGet(); 
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	
}
