package com.example;

import java.util.logging.Logger;

/**
 * Represents the cashier employee at the checkout counter.
 * <p>
 * This thread continuously consumes items from the conveyor belt (Tapis),
 * simulating the scanning process. It detects client transaction boundaries
 * using sentinel values and maintains statistics on scanned items.
 * </p>
 *
 * @author Oscar
 * @author Baptiste
 * @see Tapis
 */
public class EmpCaisse extends Thread {
    private Tapis tapis;
    private int totalArticlesScanned = 0;
    private int currentClientArticles = 0;

    public EmpCaisse(Tapis tapis) {
        this.tapis = tapis;
        this.setName("EmpCaisse");
    }

    private void log(String message) {
        Logger.getGlobal().info("[EmpCaisse] " + message);
    }

    @Override
    public void run() {
        log("Pret a servir les clients");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                int article = tapis.retirerArticle();

                if (article == -1) {
                    log("Fin client (Client-" + tapis.getCurrentClientId() + ") - "
                            + currentClientArticles + " articles scannes");
                    currentClientArticles = 0;
                    continue;
                }

                ProductEnum product = ProductEnum.fromId(article);
                String productName = (product != null) ? product.name() : "ID:" + article;

                currentClientArticles++;
                totalArticlesScanned++;
                log("Scanne: " + productName + " (article #" + currentClientArticles + ")");

                Thread.sleep(100); // Simulate scanning time

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log("Termine. Total articles scannes: " + totalArticlesScanned);
    }
}
