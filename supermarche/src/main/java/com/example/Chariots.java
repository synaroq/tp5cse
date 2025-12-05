package com.example;

import java.util.logging.Logger;

public class Chariots {
    private int nbChariotsMax;
    private int currentNbChariots;

    public Chariots(int nbChariots) {
        this.nbChariotsMax = nbChariots;
        this.currentNbChariots = nbChariots;
    }

    private void log(String message) {
        Logger.getGlobal().info("[Chariots] " + message);
    }

    public int getNbChariotsMax() {
        return nbChariotsMax;
    }

    public int getCurrentNbChariots() {
        return currentNbChariots;
    }

    public void setCurrentNbChariots(int currentNbChariots) {
        this.currentNbChariots = currentNbChariots;
    }

    public synchronized void prendreChariot(int clientId) {
        while (currentNbChariots <= 0) {
            log("Client-" + clientId + " attend un chariot... (0/" + nbChariotsMax + " dispo)");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        currentNbChariots--;
        log("Client-" + clientId + " prend un chariot (" + currentNbChariots + "/" + nbChariotsMax + " dispo)");
    }

    public synchronized void rendreChariot(int clientId) {
        currentNbChariots++;
        log("Client-" + clientId + " rend son chariot (" + currentNbChariots + "/" + nbChariotsMax + " dispo)");
        notifyAll();
    }
}
