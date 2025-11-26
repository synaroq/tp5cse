package com.example;

import java.util.List;

public class Client extends Thread {
    private List<String> listeCourses;

    public Client(List<String> listeCourses) {
        this.listeCourses = listeCourses;
   }

    @Override
    public void run() {
        
    
    }
}
