package java.HTTPServeur;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ChildServeur implements Runnable {
    private Socket sock;
    private PrintWriter writer = null;
    private BufferedReader reader = null;

    public ChildServeur(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run(){
        System.out.println("Initialisation connection Cliente");
        boolean closeConnexion = false;

        //while the cpnnection is still active we treat the different demands

        while(!sock.isClosed()){
            try {

                writer  = new PrintWriter(sock.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
                System.out.println(("TEST").getBytes().length);
                //On traite la demande du client en fonction de la commande envoyée
                String toSend = "HTTP/1.1 200 OK \r\n" +
                        "Date: Thu, 04 Nov 2004 11:30:07 GMT \r\n" +
                        "Server: Apache/1.3.12 (Unix) \r\n" +
                        "Last-Modified: Thu, 04 Nov 2004 11:30:16 GMT \r\n" +
                        "Content-Length: 4 \r\n" +
                        "Connection: close \r\n" +
                        "Content-Type: text/html \r\n" +
                        "\r\n" +
                        "TEST"+
                        "\n\n";

                //Traitement des données reçues
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
        String test=reader.readLine();
      return test;
    }
}
