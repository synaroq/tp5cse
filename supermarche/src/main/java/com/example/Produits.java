package com.example;

public enum Produits {
    SUCRE(0),
    FARINE(1),
    BEURRE(2),
    LAIT(3);

    private int id;

    Produits(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
