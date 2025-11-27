package com.example;

import java.util.List;

public class Client extends Thread {
    private List<Produit> listeCourses;
    private List<Produit> panier;


   public List<Produit> getListeCourses() {
        return listeCourses;
    }

    private void generateListeCourses(Entrepot entrepot) {
        int nbProduct = (int)(Math.random() * 10) + 1; 
        int productId = 0;
        for (int i = 0; i < nbProduct; i++) {
            productId = (int)(Math.random() * entrepot.getNbProducts());
            Produit produit = entrepot.getProductById(productId);

            listeCourses.add(produit);
        
        }
    }

    private void acheterProduit(Produit produit) {
        if (listeCourses.contains(produit)) {
            panier.add(produit);
            listeCourses.remove(produit);
            
        }
        
    }


    @Override
    public void run() {
        
    
    }
}
