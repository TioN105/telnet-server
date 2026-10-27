package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) {
        int port = 12345;
        int max_threads = 10;    

        // Zpracovani vstupnich parametru (pokud byly zadany)
        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatny port. Pouzije se vychozi: " + port);
            }
        }
        if (args.length >= 2) {
            try {
                max_threads = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatny pocet vlaken. Pouzije se vychozi: " + max_threads);
            }
        }

        // Vytvoreni thread poolu (omezeni maximalniho poctu vlaken/klientu)
        ExecutorService executor = Executors.newFixedThreadPool(max_threads);

        // Hlavni smycka serveru
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server nasloucha na portu " + port + " (Max vlaken: " + max_threads + ")");

            while (true) {
                // Prijmeme nove pripojeni od klienta
                Socket clientSocket = serverSocket.accept();
                
                // Predame klienta k obsluze do pripraveneho fondu vlaken
                executor.execute(new ClientThread(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Chyba serveroveho soketu: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}