package com.example;

public enum ProduitEnum {
    SUCRE(0),
    FARINE(1),
    BEURRE(2),
    LAIT(3);

    private final int id;

    ProduitEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return switch (this) {
            case SUCRE -> "Sucre";
            case FARINE -> "Farine";
            case BEURRE -> "Beurre";
            case LAIT -> "Lait";
            default -> "Unknown";
        };
    }
}
