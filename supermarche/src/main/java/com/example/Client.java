package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Client extends Thread {
    private static int clientCounter = 0;
    private final int clientId;
    private final Map<ProductEnum, Integer> listeCourses = new HashMap<>();
    private final List<ProductEnum> panier = new ArrayList<>();
    private Chariots chariots;
    private List<Rayon> rayons;
    private Tapis tapis;

    public Client(Entrepot entrepot, List<Rayon> rayons, Chariots chariots, Tapis tapis) {
        this.clientId = ++clientCounter;
        this.setName("Client-" + clientId);
        this.rayons = rayons;
        this.chariots = chariots;
        this.tapis = tapis;
        generateListeCourses(entrepot);
    }

    public int getClientId() {
        return clientId;
    }

    public Map<ProductEnum, Integer> getListeCourses() {
        return listeCourses;
    }

    private void log(String message) {
        Logger.getGlobal().info("[Client-" + clientId + "] " + message);
    }

    private void generateListeCourses(Entrepot entrepot) {
        List<ProductEnum> availableProducts = entrepot.getAvailableProducts();
        for (ProductEnum produit : availableProducts) {
            int quantity = (int) (Math.random() * 6);
            if (quantity > 0) {
                listeCourses.put(produit, quantity);
            }
        }
        log("Liste de courses: " + listeCourses);
    }

    private void acheterProduit(ProductEnum produit, int quantity) {
        for (int i = 0; i < quantity; i++) {
            panier.add(produit);
        }
        listeCourses.remove(produit);
        log("A pris " + quantity + "x " + produit + " | Panier: " + formatPanier());
    }

    private String formatPanier() {
        Map<ProductEnum, Integer> panierCount = new HashMap<>();
        for (ProductEnum p : panier) {
            panierCount.put(p, panierCount.getOrDefault(p, 0) + 1);
        }
        return panierCount.toString();
    }

    @Override
    public void run() {
        log("Entre dans le supermarche");

        // Take a cart
        chariots.prendreChariot(clientId);

        List<Map.Entry<ProductEnum, Integer>> shoppingList = new ArrayList<>(listeCourses.entrySet());

        // Shop for products
        for (Map.Entry<ProductEnum, Integer> entry : shoppingList) {
            ProductEnum produit = entry.getKey();
            int quantity = entry.getValue();

            log("Cherche " + quantity + "x " + produit);

            // Find the corresponding shelf
            for (Rayon rayon : rayons) {
                if (rayon.getProduit() == produit) {
                    ProductEnum pickedProduct = null;
                    int attempts = 0;
                    while (pickedProduct == null) {
                        pickedProduct = rayon.pickProducts(quantity, clientId);
                        if (pickedProduct == null) {
                            attempts++;
                            if (attempts % 5 == 0) {
                                log("Attend reapprovisionnement de " + produit + "...");
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }
                    acheterProduit(pickedProduct, quantity);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    break;
                }
            }
        }

        // Go to checkout
        log("Se dirige vers la caisse avec " + panier.size() + " articles");

        List<Integer> articlesToDeposit = panier.stream()
                .map(ProductEnum::getId)
                .toList();
        try {
            tapis.deposerArticles(articlesToDeposit, clientId);
            tapis.waitForCheckoutComplete(clientId);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }

        log("Paiement termine!");

        // Return the cart
        chariots.rendreChariot(clientId);

        log("Quitte le supermarche. Achats: " + formatPanier());
    }
}
