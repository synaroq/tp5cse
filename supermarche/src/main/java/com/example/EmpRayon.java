package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpRayon extends Thread {

    private Map<ProductEnum,Integer> carriedProducts = new HashMap<>();
    private final int MAX_CARRY_PER_PRODUCT = 5;
    private List<Rayon> rayons;
    private final Entrepot entrepot;

    public EmpRayon(List<Rayon> rayons, Entrepot entrepot) {
        this.rayons = rayons;
        this.entrepot = entrepot;
    }


    /** 
     * 
     * Add products to the rayon 
     */
    public void addProductAmountToRayon(Rayon rayon, int amount) {
        ProductEnum produit = rayon.getProduit();
        int currentAmount = carriedProducts.getOrDefault(produit, 0);
        carriedProducts.put(produit, currentAmount - amount);
        int remaining = rayon.refill(amount);
        if (remaining > 0) {
            carriedProducts.put(produit, carriedProducts.getOrDefault(produit, 0) + remaining);
        }

    
    }

    private void refillCarryFromEntrepot() {
        for (ProductEnum produit : entrepot.getAvailableProducts()) {
            carriedProducts.put(produit, MAX_CARRY_PER_PRODUCT);
        }
    } 

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //refill carried products from entrepot
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            refillCarryFromEntrepot();
            //moving to first rayon from entrepot
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
            
            for (Rayon rayon : rayons) {
                
                ProductEnum produit = rayon.getProduit();
                int carriedAmount = carriedProducts.getOrDefault(produit, 0);
                if (carriedAmount > 0) {
                    addProductAmountToRayon(rayon, carriedAmount);
                }
                //moving to next rayon
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
            }
            //moving back to entrepot
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }



    }


    
}
