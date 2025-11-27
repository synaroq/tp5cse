package com.example;

import java.util.List;

public class Rayon{
    private Produit produit; 
    private int nbProductMax;
    private int currentAmountproducts = nbProductMax;
    
    public Rayon( Produit produit, int nbProductMax) {
        this.produit = produit;
        this.nbProductMax = nbProductMax;
    }

    public Produit getProduit() {
        return produit;
    }

    public int getNbProductMax() {
        return nbProductMax;
    }

    public int getCurrentAmountproducts() {
        return currentAmountproducts;
    }

    public void setCurrentAmountproducts(int currentAmountproducts) {
        this.currentAmountproducts = currentAmountproducts;
    }



}