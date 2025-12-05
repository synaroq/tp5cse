package com.example;

import java.util.List;
import java.util.logging.Logger;

public class Tapis {
    private final Integer[] tapis;
    private final int max_articles;
    private int start;
    private int end;
    private int size;
    private boolean checkoutComplete = false;
    private int currentClientId = -1;

    public Tapis(int max_articles) {
        this.tapis = new Integer[max_articles];
        this.max_articles = max_articles;
        this.start = 0;
        this.end = 0;
        this.size = 0;
    }

    private void log(String message) {
        Logger.getGlobal().info("[Tapis] " + message);
    }

    public synchronized void ajouterArticle(int article, int clientId) {
        while (size >= max_articles) {
            try {
                log("Plein! Client-" + clientId + " attend...");
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

    public synchronized int retirerArticle() throws InterruptedException {
        while (size == 0) {
            wait();
        }
        int article = tapis[start];
        tapis[start] = null;
        start = (start + 1) % max_articles;
        size--;

        if (article == -1) {
            checkoutComplete = true;
            notifyAll();
        } else {
            notifyAll();
        }
        return article;
    }

    // NOT synchronized - each ajouterArticle call handles its own synchronization
    public void deposerArticles(List<Integer> articles, int clientId) throws InterruptedException {
        synchronized (this) {
            checkoutComplete = false;
            currentClientId = clientId;
        }
        log("Client-" + clientId + " depose " + articles.size() + " articles");
        for (int article : articles) {
            ajouterArticle(article, clientId);
            Thread.sleep(20);
        }
        ajouterArticle(-1, clientId); // Sentinel
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == max_articles;
    }

    public int getCurrentAmountproducts() {
        return size;
    }

    public synchronized int getCurrentClientId() {
        return currentClientId;
    }

    public synchronized void waitForCheckoutComplete(int clientId) throws InterruptedException {
        while (!checkoutComplete) {
            wait();
        }
        checkoutComplete = false;
        log("Client-" + clientId + " a termine son passage en caisse");
    }
}