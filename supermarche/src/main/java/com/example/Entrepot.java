package com.example;

import java.util.List;

public class Entrepot{
    private final List<ProduitEnum> availableProducts;

    public Entrepot(List<ProduitEnum> availableProducts) {
        this.availableProducts = availableProducts;
    }


    public List<ProduitEnum> getAvailableProducts() {
        return availableProducts;
    }

    public ProduitEnum getProductById(int id) {
        for (ProduitEnum produit : availableProducts) {
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