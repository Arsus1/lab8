package ru.ifmo.lab;

import java.io.IOException;

import ru.ifmo.lab.network.Client;

/**
 * Главный класс приложения
 */


public class Main {


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String mode = System.getProperty("mode", "cli").toLowerCase();
        switch (mode){
            case "cli":
                new CLI();
                break;

            case "client":
                new Client(args).run();
                break;

        }

    }

}
