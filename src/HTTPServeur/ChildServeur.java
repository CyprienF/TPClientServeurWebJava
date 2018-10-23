package HTTPServeur;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ChildServeur implements Runnable {
    private Socket sock;
    private PrintWriter writer = null;
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
                writer  = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream())
                //On attend la demande du client
                String reponse = read();
                InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

                //On affiche quelques infos, pour le débuggage
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + reponse + "\n";
                System.err.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                String toSend = "";

                //Traitement des donnée reçut
                writer.write(toSend);
                //Il FAUT IMPERATIVEMENT UTILISER flush()
                //Sinon les données ne seront pas transmises au client
                //et il attendra indéfiniment
                writer.flush();
                
                if(closeConnexion){
                    System.err.println("COMMANDE CLOSE DETECTEE ! ");
                    writer = null;
                    reader = null;
                    sock.close();
                    break;
                }
            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
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
