package com.example;

import java.util.List;

public class Tapis {
    private final Integer[] tapis;
    private final int max_articles;
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

    public synchronized void ajouterArticle(int article) {
        while (size >= max_articles) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        tapis[end] = article;
        end = (end + 1) % max_articles;
        size++;
        notifyAll();
    }

    public synchronized int retirerArticle() {
        while (size == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Thread interrupted");
            }
        }
        int article = tapis[start];
        tapis[start] = null;
        start = (start + 1) % max_articles;
        size--;
        notifyAll();
        return article;
    }

    public synchronized void deposerArticles(List<Integer> articles) {
        for (int article : articles) {
            ajouterArticle(article);
        }
        ajouterArticle(-1);
        
    }

    public boolean estPlein() {
        return size == max_articles;
    }

    public int getCurrentAmountproducts() {
        return size;
    }

    
}