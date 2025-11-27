package com.example;

import java.util.List;

public class Entrepot{
    private List<Produit> availableProducts;

    public Entrepot(List<Produit> availableProducts) {
        this.availableProducts = availableProducts;
    }


    public List<Produit> getAvailableProducts() {
        return availableProducts;
    }

    public Produit getProductById(int id) {
        for (Produit produit : availableProducts) {
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