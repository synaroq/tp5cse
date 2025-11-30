package com.example;

import java.util.ArrayList;
import java.util.List;

public class EmpRayon extends Thread {

    private List<Produit> carriedProducts = new ArrayList<>();
    private List<Rayon> rayons;

    public EmpRayon(List<Rayon> rayons) {
        this.rayons = rayons;
    }

    public void addProduct(Produit produit) {
        if (!canCarry(produit)) {
            throw new IllegalArgumentException("Employee cannot carry this product: " + produit.getName());
        }
        
        carriedProducts.add(produit);
    }

    /* 
     * Can carry only 5 times the same product
     */
    private boolean canCarry(Produit produit) {
        int count = 0;
        for (Produit p : carriedProducts) {
            if (p.getId() == produit.getId()) {
                count++;
            }
        }
        return count < 5;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
        }



    }


    
}
