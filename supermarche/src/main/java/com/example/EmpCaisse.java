package com.example;
public class EmpCaisse extends Thread {
    private Tapis tapis;

    public EmpCaisse(Tapis tapis) {
        this.tapis = tapis;
    }

    @Override
    public void run() {
        while (true) {
            if (tapis.getCurrentAmountproducts()>0){
                tapis.retirerArticle();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException ex) {
                }
               
            }
        }
    }
    
}
