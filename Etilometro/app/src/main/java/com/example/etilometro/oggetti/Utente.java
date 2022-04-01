package com.example.etilometro.oggetti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Utente implements Serializable {

    private String nome;
    private double peso;
    private String sesso;
    private String account;
    private List<Acquisizioni> acquisizioniList;


    public Utente(String nome, double peso, String sesso, String account) {
        this.nome = nome;
        this.peso = peso;
        this.sesso = sesso;
        this.account = account;
        acquisizioniList = new ArrayList<>();
    }

    public Utente(String nome, double peso, String sesso, String account, List<Acquisizioni> acquisizioniList) {
        this.nome = nome;
        this.peso = peso;
        this.sesso = sesso;
        this.account = account;
        this.acquisizioniList = acquisizioniList;
    }

    @Override
    public String toString() {
        return "Utente{" +
                ", nome='" + nome + '\'' +
                ", peso=" + peso +
                ", sesso='" + sesso + '\'' +
                ", account='" + account + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
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

    public String getAccount() {
        return account;
    }

    public List<Acquisizioni> getAcquisizioniList() {
        return acquisizioniList;
    }

    public void setAcquisizioniList(List<Acquisizioni> acquisizioniList) {
        this.acquisizioniList = acquisizioniList;
    }
}
