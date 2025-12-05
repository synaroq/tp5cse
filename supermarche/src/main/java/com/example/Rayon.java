package com.example;

import java.util.logging.Logger;

public class Rayon {
    private ProductEnum product;
    private int nbProductMax;
    private int currentAmountProducts;

    public Rayon(ProductEnum produit, int nbProductMax) {
        this.product = produit;
        this.nbProductMax = nbProductMax;
        this.currentAmountProducts = nbProductMax;
    }

    private void log(String message) {
        Logger.getGlobal().info("[Rayon " + product + "] " + message);
    }

    public ProductEnum getProduit() {
        return product;
    }

    /**
     * Refill the shelf with a given number of products.
     * 
     * @param nbproducts
     * @return the remaining products that couldn't fit on the shelf.
     */
    public synchronized int refill(int nbproducts) {
        int spaceAvailable = nbProductMax - currentAmountProducts;
        int toAdd = Math.min(nbproducts, spaceAvailable);
        currentAmountProducts += toAdd;
        int remaining = nbproducts - toAdd;
        if (toAdd > 0) {
            log("Reapprovisionne +" + toAdd + " (stock: " + currentAmountProducts + "/" + nbProductMax + ")");
        }
        return remaining;
    }

    public synchronized boolean isFull() {
        return currentAmountProducts >= nbProductMax;
    }

    public synchronized ProductEnum pickProducts(int amount, int clientId) {
        if (amount <= currentAmountProducts) {
            currentAmountProducts -= amount;
            log("Client-" + clientId + " prend " + amount + " (reste: " + currentAmountProducts + "/" + nbProductMax
                    + ")");
            return product;
        } else {
            return null; // Not enough products available
        }
    }

    public int getNbProductMax() {
        return nbProductMax;
    }

    public synchronized int getCurrentAmountProducts() {
        return currentAmountProducts;
    }

    public void setCurrentAmountProducts(int currentAmountProducts) {
        this.currentAmountProducts = currentAmountProducts;
    }
}