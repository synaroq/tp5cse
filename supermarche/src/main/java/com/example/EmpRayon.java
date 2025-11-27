package com.example;

import java.util.List;

public class EmpRayon {

    private List<Produit> carriedProducts;  

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


    
}
