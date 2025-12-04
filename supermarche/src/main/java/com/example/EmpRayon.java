package com.example;

import java.util.ArrayList;
import java.util.List;

public class EmpRayon extends Thread {

    private List<ProductEnum> carriedProducts = new ArrayList<>();
    private List<Rayon> rayons;

    public EmpRayon(List<Rayon> rayons) {
        this.rayons = rayons;
    }

    public void addProduct(ProductEnum produit) {
        if (!canCarry(produit)) {
            throw new IllegalArgumentException("Employee cannot carry this product: " + produit.getName());
        }
        
        carriedProducts.add(produit);
    }

    /* 
     * Can carry only 5 times the same product
     */
    private boolean canCarry(ProductEnum produit) {
        int count = 0;
        for (ProductEnum p : carriedProducts) {
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
