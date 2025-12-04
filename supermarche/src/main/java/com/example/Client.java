package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends Thread {
    private final Map<ProductEnum,Integer> listeCourses = new HashMap<>();
    private final List<ProductEnum> panier = new ArrayList<>();
    private Entrepot entrepot;
    private Chariots chariots;
    private List<Rayon> rayons;
    private Tapis tapis;


    public Client(Entrepot entrepot, List<Rayon> rayons, Chariots chariots, Tapis tapis) {
        this.entrepot = entrepot;
        this.rayons = rayons;
        this.chariots = chariots;
        this.tapis = tapis;
        generateListeCourses(entrepot);
    }

   public Map<ProductEnum,Integer> getListeCourses() {
        return listeCourses;
    }

    private void generateListeCourses(Entrepot entrepot) {
        List<ProductEnum> availableProducts = entrepot.getAvailableProducts();
        for (ProductEnum produit : availableProducts) {
            int quantity = (int) (Math.random() * 6);
            listeCourses.put(produit, quantity);
        }    

                 
        
    }

    private void acheterProduit(ProductEnum produit, int quantity) {
        for (int i = 0; i < quantity; i++) {
            panier.add(produit);
            int currentCount = listeCourses.get(produit);
            if (currentCount > 1) {
                listeCourses.put(produit, currentCount - 1);
            } 
            else {
                listeCourses.remove(produit);
            }
        }
    }

    


    @Override
    public void run() {
          // Take a cart
        chariots.prendreChariot();
    // Shop for products
        for (Map.Entry<ProductEnum, Integer> entry : listeCourses.entrySet()) {
            ProductEnum produit = entry.getKey();
            int quantity = entry.getValue();

            // Find the corresponding shelf
            for (Rayon rayon : rayons) {
                if (rayon.getProduit() == produit) {
                    ProductEnum pickedProduct = null;
                    while (pickedProduct == null) {
                        pickedProduct = rayon.pickProducts(quantity);
                    }
                    acheterProduit(pickedProduct, quantity);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                    }
                    break;
                }

            }
        }
        // Go to checkout and place items on the conveyor belt
        List<Integer> articlesToDeposit = panier.stream()
                .map(ProductEnum::getId)
                .toList();
        try {
            tapis.deposerArticles(articlesToDeposit);
        } catch (InterruptedException ex) {
        }
        //TODO synchronize with EmpCaisse to wait until all articles are processed
        // Return the cart
        chariots.rendreChariot();
    
        
    
    }
}
