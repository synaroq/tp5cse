package com.example;


public class Rayon{
    private ProductEnum product; 
    private int nbProductMax;
    private int currentAmountproducts;
    
    public Rayon( ProductEnum produit, int nbProductMax) {
        this.product = produit;
        this.nbProductMax = nbProductMax;
        this.currentAmountproducts = nbProductMax;
    }

    public ProductEnum getProduit() {
        return product;
    }

    /**
     * Refill the shelf with a given number of products.
     * @param nbproducts
     * @return the remaining products that couldn't fit on the shelf.
     */
    public int refill(int nbproducts) {
       
        int spaceAvailable = nbProductMax - currentAmountproducts;
        if (nbproducts <= spaceAvailable) {
            currentAmountproducts += nbproducts;
            return 0; // All products fit
        } else {
            currentAmountproducts = nbProductMax;
            return nbproducts - spaceAvailable; // Return remaining products
        }
    }

    public synchronized ProductEnum pickProducts(int amount) {
        if (amount <= currentAmountproducts) {
            currentAmountproducts -= amount;
            return product;
        } else {
            return null; // Not enough products available
        }
        
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