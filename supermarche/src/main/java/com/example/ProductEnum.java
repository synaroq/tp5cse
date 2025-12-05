package com.example;

public enum ProductEnum {
    SUCRE(0),
    FARINE(1),
    BEURRE(2),
    LAIT(3);

    private final int id;

    ProductEnum(int id) {
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

    public static ProductEnum fromId(int id) {
        for (ProductEnum p : values()) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }
}
