package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main {
    private Chariots chariots = new Chariots(6);

    private List<Rayon> rayons = new ArrayList<>() {
        {
            add(new Rayon(ProductEnum.BEURRE, 5));
            add(new Rayon(ProductEnum.FARINE, 5));
            add(new Rayon(ProductEnum.LAIT, 5));
            add(new Rayon(ProductEnum.SUCRE, 5));
        }
    };

    private Tapis tapis;
    private List<Client> clients;
    private EmpCaisse employeCaisse;
    private Entrepot entrepot;
    private List<ProductEnum> availableProducts = new ArrayList<>() {
        {
            add(ProductEnum.BEURRE);
            add(ProductEnum.FARINE);
            add(ProductEnum.LAIT);
            add(ProductEnum.SUCRE);
        }
    };
    private EmpRayon employeRayon;

    public void runSimulation() {
        System.out.println("=".repeat(60));
        System.out.println("        SIMULATION SUPERMARCHE DEMARREE");
        System.out.println("=".repeat(60));

        entrepot = new Entrepot(availableProducts);

        employeRayon = new EmpRayon(rayons, entrepot);
        employeRayon.start();

        tapis = new Tapis(10);

        employeCaisse = new EmpCaisse(tapis);
        employeCaisse.start();

        clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Client client = new Client(new Entrepot(availableProducts), rayons, chariots, tapis);
            clients.add(client);
            client.start();
        }

        for (Client client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Wait for tapis to be empty
        while (!tapis.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }

        employeCaisse.interrupt();
        employeRayon.interrupt();

        try {
            employeCaisse.join();
            employeRayon.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=".repeat(60));
        System.out.println("        SIMULATION TERMINEE");
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) {
        Logger logger = Logger.getGlobal();
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        });
        logger.addHandler(handler);

        Main supermarche = new Main();
        supermarche.runSimulation();
    }
}