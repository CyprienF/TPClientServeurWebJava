package HTTPServeur;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Scanner;

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
                writer.write(getFile(reponse));
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
    };

    private String getFile(String reponses){
        String[] route =reponses.split(" ");

        File file = new File("./src/main/resources/"+route[1]);
        String toSend;
        try{
            Scanner sc = new Scanner(file);
            toSend = "HTTP/1.1 200 OK \r\n" +
                    "Date: "+  OffsetDateTime.now().toString() +"\r\n" +
                    "Server: Apache/1.3.12 (Unix) \r\n" +
                        "Last-Modified: "+ new Timestamp(file.lastModified()) +"\r\n" +
                    "Content-Length: "+ file.length() +"\r\n" +
                    "Connection: active \r\n" +
                    "Content-Type: text/html \r\n" +
                    "\r\n";
            while (sc.hasNextLine())
                toSend+=sc.nextLine();

            toSend+="\n\n";
            return toSend;
        }catch (Exception e) {
            System.err.println("Le fichier n'exisite pas");
            toSend="HTTP/1.1 404 Not Found\r\n" +
            "Date: Sun, 18 Oct 2012 10:36:20 GMT \r\n" +
            "Server: Apache/2.2.14 (Win32) \r\n" +
            "Content-Length: 230 \r\n" +
            "Content-Type: text/html; charset=iso-8859-1 \r\n" +
            "Connection: Closed \r\n" +
            "\r\n";
            toSend+="<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "   <title>404 Not Found</title>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "   <h1>Not Found</h1>\n" +
                    "   <p>The requested URL "+route[1]+"was not found on this server.</p>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
            return toSend;

        }

    }
}
