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

    public String getName() {
        switch (this) {
            case SUCRE:
                return "Sucre";
            case FARINE:
                return "Farine";
            case BEURRE:
                return "Beurre";
            case LAIT:
                return "Lait";
            default:
                return "Unknown";
        }
    }
}
