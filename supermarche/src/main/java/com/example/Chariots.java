package com.example;
/*  */
public class Chariots {
    private int nbChariotsMax;
    private int currentNbChariots = nbChariotsMax;

    public Chariots(int nbChariots) {
        this.nbChariotsMax = nbChariots;
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

    public synchronized boolean prendreChariot() {
        if (currentNbChariots > 0) {
            currentNbChariots--;
            return true;
        }
        return false;
    }

    public synchronized void rendreChariot() {
        if (currentNbChariots < nbChariotsMax) {
            currentNbChariots++;
        }
    }
    
}
