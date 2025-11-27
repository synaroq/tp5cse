package com.example;

import java.util.List;

public class Rayon{
    private Produit produit; 
    private int nb_product_max;
    private int currentAmountproducts = nb_product_max;
    
    public Rayon( Produit produit, int nb_product_max) {
        this.produit = produit;
        this.nb_product_max = nb_product_max;
    }

    public Produit getProduit() {
        return produit;
    }

    public int getNb_product_max() {
        return nb_product_max;
    }

    public int getCurrentAmountproducts() {
        return currentAmountproducts;
    }

    public void setCurrentAmountproducts(int currentAmountproducts) {
        this.currentAmountproducts = currentAmountproducts;
    }

    

}