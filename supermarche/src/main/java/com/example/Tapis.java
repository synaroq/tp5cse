package com.example;

public class Tapis {
    private Integer[] tapis;
    private int max_articles;
    private int start;
    private int end;
    private int size;

    public Tapis(int max_articles) {
        this.tapis = new Integer[max_articles];
        this.max_articles = max_articles;
        this.start = 0;
        this.end = 0;
        this.size = 0;
    }

    public void ajouterArticle(int article) {
        if (size < max_articles) {
            tapis[end] = article;
            end = (end + 1) % max_articles;
            size++;
        } else {
            throw new IllegalStateException("Le tapis est plein");
        }
    }

    public int retirerArticle() {
        if (size > 0) {
            int article = tapis[start];
            tapis[start] = null; // Optional: Clear the reference
            start = (start + 1) % max_articles;
            size--;
            return article;
        }
        throw new IllegalStateException("La caisse est vide");
    }

    public void deposerArticle() {
        // Logic to handle client deposit
        if (!estPlein()) {
            // Simulate a client depositing an article
            int article = (int) (Math.random() * 100); // Example: Random article number
            ajouterArticle(article);
            System.out.println("Client a déposé l'article: " + article);
        } else {
            System.out.println("Le tapis est plein, le client ne peut pas déposer d'article.");
        }
    }

    public boolean estPlein() {
        return size == max_articles;
    }

    public int getCurrentAmountproducts() {
        return size;
    }

    
}