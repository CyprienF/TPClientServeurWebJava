package HTTPServeur;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ChildServeur implements Runnable {
    private Socket sock;
    private OutputStream writer = null;
    private BufferedInputStream reader = null;

    public ChildServeur(Socket sock) {
        this.sock = sock;
    }

    public void run(){
        System.out.println("Initialisation connection Cliente");
        boolean closeConnexion = false;

        //while the cpnnection is still active we treat the different demands

        while(!sock.isClosed()){

            try {
                writer  = sock.getOutputStream();
                reader = new BufferedInputStream(sock.getInputStream())
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String read() throws IOException {
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
