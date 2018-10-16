package HTTPback;

import java.io.IOException;
import java.net.ServerSocket;

public class Serveur {
    private static final int PORTECOUTE=1026;
    private ServerSocket socketEcouteSeveur;

    public Serveur() throws IOException {
        this.socketEcouteSeveur = new ServerSocket(PORTECOUTE);
    }
    
}
