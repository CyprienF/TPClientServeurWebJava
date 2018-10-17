package HTTPServeur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServeur {
    private static final int PORTECOUTE=1026;
    private ServerSocket socketEcouteSeveur;
    private boolean isRunning = true;
    public MainServeur() throws IOException {
        this.socketEcouteSeveur = new ServerSocket(PORTECOUTE,6);
    }

    public void runServeur() throws IOException {
        while(true){
            Socket nouvelleConnection = this.socketEcouteSeveur.accept();

        }
    }

    public void childServeur(Socket nouvelleConnection){
        Socket clientConnection = nouvelleConnection;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning == true){

                }
            }
        });
    }

}
