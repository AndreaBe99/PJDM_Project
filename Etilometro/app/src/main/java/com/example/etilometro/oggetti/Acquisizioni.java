package com.example.etilometro.oggetti;

import java.io.Serializable;

public class Acquisizioni implements Serializable {

    private  double gradi, peso;
    private String data, tvdrink;

    @Override
    public String toString() {
        return "Acquisizioni{" +
                "gradi=" + gradi +
                ", peso=" + peso +
                ", data='" + data + '\'' +
                ", tvdrink='" + tvdrink + '\'' +
                '}';
    }

    public Acquisizioni(double gradi, String data, String tvdrink, double peso) {
        this.gradi = gradi;
        this.data = data;
        this.tvdrink = tvdrink;
        this.peso = peso;
    }

    public double getGradi() {
        return gradi;
    }

    public String getData() {
        return data;
    }

    public String getTvdrink() {
        return tvdrink;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
