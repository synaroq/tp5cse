package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents the shelf restocking employee.
 * <p>
 * This thread periodically checks shelf levels and restocks them by fetching
 * products from the warehouse. It acts as a producer in the shelf resource
 * management, ensuring products remain available for customers.
 * </p>
 *
 * @author Oscar
 * @author Baptiste
 * @version 1.0
 * @see Rayon
 * @see Entrepot
 */
public class EmpRayon extends Thread {

    private Map<ProductEnum, Integer> carriedProducts = new HashMap<>();
    private final int MAX_CARRY_PER_PRODUCT = 5;
    private List<Rayon> rayons;
    private final Entrepot entrepot;

    public EmpRayon(List<Rayon> rayons, Entrepot entrepot) {
        this.rayons = rayons;
        this.entrepot = entrepot;
        this.setName("Rogée du rayon lait");
    }

    private void log(String message) {
        Logger.getGlobal().info("[EmpRayon] " + message);
    }

    /**
     * Add products to the rayon
     */
    public synchronized void addProductAmountToRayon(Rayon rayon, int amount) {
        ProductEnum produit = rayon.getProduit();
        int currentAmount = carriedProducts.getOrDefault(produit, 0);

        if (!rayon.isFull() && currentAmount > 0) {
            int toAdd = Math.min(amount, currentAmount);
            int remaining = rayon.refill(toAdd);
            int added = toAdd - remaining;
            carriedProducts.put(produit, currentAmount - added);
        }
    }

    public boolean isFull() {
        for (ProductEnum produit : entrepot.getAvailableProducts()) {
            if (carriedProducts.getOrDefault(produit, 0) < MAX_CARRY_PER_PRODUCT) {
                return false;
            }
        }
        return true;
    }

    private void refillCarryFromEntrepot() {
        log("Va chercher des produits a l'entrepot");
        for (ProductEnum produit : entrepot.getAvailableProducts()) {
            carriedProducts.put(produit, MAX_CARRY_PER_PRODUCT);
        }
        log("Charge: " + carriedProducts);
    }

    @Override
    public void run() {
        log("Commence son service");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }

            if (!isFull()) {
                refillCarryFromEntrepot();
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }

            for (Rayon rayon : rayons) {
                ProductEnum produit = rayon.getProduit();
                int carriedAmount = carriedProducts.getOrDefault(produit, 0);
                if (carriedAmount > 0 && !rayon.isFull()) {
                    addProductAmountToRayon(rayon, carriedAmount);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        log("Termine son service");
    }
}
