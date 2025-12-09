package com.example;

/**
 * Enumeration of available product types in the supermarket.
 * <p>
 * Each product has a unique identifier used for serialization on the
 * conveyor belt. Provides utility methods for ID-based lookup and
 * display names.
 * </p>
 *
 * @author Oscar
 * @author Baptiste
 */
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
