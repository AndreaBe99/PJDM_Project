package com.example.etilometro.oggetti;

public class Drink {
    private final String name;
    private final int gradi;
    private final int litri;
    private final String img;
    private int quantita;


    public Drink(String name, int gradi, int litri, String img) {
        this.name = name;
        this.gradi = gradi;
        this.litri = litri;
        this.img = img;
        this.quantita = 0;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "name='" + name + '\'' +
                ", gradi='" + gradi + '\'' +
                ", litri='" + litri + '\'' +
                ", quantita=" + quantita +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getGradi() { return gradi; }

    public int getLitri() { return litri; }

    public String getImg() { return img; }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
