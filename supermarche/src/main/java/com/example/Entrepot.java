package com.example;

import java.util.List;

/**
 * Represents the warehouse storing available product types.
 * <p>
 * Serves as an infinite source of products for restocking shelves.
 * Provides lookup functionality for available products by type or ID.
 * </p>
 *
 * @author Oscar
 * @author Baptiste
 * @see EmpRayon
 * @see ProductEnum
 */
public class Entrepot {
    private final List<ProductEnum> availableProducts;

    public Entrepot(List<ProductEnum> availableProducts) {
        this.availableProducts = availableProducts;
    }

    public List<ProductEnum> getAvailableProducts() {
        return availableProducts;
    }

    public ProductEnum getProductById(int id) {
        for (ProductEnum produit : availableProducts) {
            if (produit.getId() == id) {
                return produit;
            }
        }
        return null; // or throw an exception if preferred
    }

    public int getNbProducts() {
        return availableProducts.size();
    }

}