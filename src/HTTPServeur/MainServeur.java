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
        while(isRunning){
            Socket nouvelleConnection = this.socketEcouteSeveur.accept();
        }
    }

    public void childServeur(Socket nouvelleConnection){
        Socket clientConnection = nouvelleConnection;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning == true){
                    try {
                        //On attend une connexion d'un client
                        Socket client = socketEcouteSeveur.accept();

                        //Une fois reçue, on la traite dans un thread séparé
                        System.out.println("Connexion cliente reçue.");
                        Thread t = new Thread(new ChildServeur(client));
                        t.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    socketEcouteSeveur.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    socketEcouteSeveur = null;
                }
                }
        });
    }

    public void closeConnection(){
        isRunning= false;
    }

}
