package com.example;

import java.util.List;

public class Entrepot{
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