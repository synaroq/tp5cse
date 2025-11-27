package com.example;

public enum Produit {
    SUCRE(0),
    FARINE(1),
    BEURRE(2),
    LAIT(3);

    private int id;

    Produit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
