package com.example;


public class Rayon{
    private ProduitEnum produit; 
    private int nbProductMax;
    private int currentAmountproducts;
    
    public Rayon( ProduitEnum produit, int nbProductMax) {
        this.produit = produit;
        this.nbProductMax = nbProductMax;
        this.currentAmountproducts = nbProductMax;
    }

    public ProduitEnum getProduit() {
        return produit;
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