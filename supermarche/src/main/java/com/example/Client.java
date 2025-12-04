package com.example;

import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
    private final List<ProduitEnum> listeCourses = new ArrayList<>();
    private final List<ProduitEnum> panier = new ArrayList<>();
    private Entrepot entrepot;
    private Chariots chariots;
    private List<Rayon> rayons;


    public Client(Entrepot entrepot, List<Rayon> rayons, Chariots chariots) {
        this.entrepot = entrepot;
        this.rayons = rayons;
        this.chariots = chariots;
        generateListeCourses(entrepot);
    }

   public List<ProduitEnum> getListeCourses() {
        return listeCourses;
    }

    private void generateListeCourses(Entrepot entrepot) {
        int nbProduct = (int)(Math.random() * 10) + 1; 
        int productId = 0;
        for (int i = 0; i < nbProduct; i++) {
            productId = (int)(Math.random() * entrepot.getNbProducts());
            ProduitEnum produit = entrepot.getProductById(productId);

            listeCourses.add(produit);
        
        }
    }

    private void acheterProduit(ProduitEnum produit) {
        if (listeCourses.contains(produit)) {
            panier.add(produit);
            listeCourses.remove(produit);
            
        }
        
    }


    @Override
    public void run() {
          // Take a cart
    // Shop for products
    // Go to checkout
    // Return cart
        
    
    }
}
