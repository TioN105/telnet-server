package utb.fai;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Echo - cteni dat od klienta a okamzite odesilani zpet
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
            
        } catch (IOException e) {
            // Klient se odpojil nebo nastala chyba
        } finally {
            // Zajistime bezpecne uzavreni soketu
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // Chybu pri uzavirani muzeme ignorovat
            }
        }
    }
}