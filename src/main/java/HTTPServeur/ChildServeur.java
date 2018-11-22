package HTTPServeur;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Scanner;

public class ChildServeur implements Runnable {
    private Socket sock;
    private OutputStream writer = null;
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
                //sock.setSoTimeout(10000);
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                writer  = sock.getOutputStream();
                //On attend la demande du client
                String reponse = read();
                InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

                //On affiche quelques infos, pour le débuggage
                String debug = "";
                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + reponse + "\n";

                if(reponse==null)
                    closeConnexion =true;

                System.err.println("\n" + debug);

                //On traite la demande du client en fonction de la commande envoyée
                String toSend =getFile(reponse);

                //Traitement des données reçues

                writer.write(toSend.getBytes());
                //Il FAUT IMPERATIVEMENT UTILISER flush()
                //Sinon les données ne seront pas transmises au client
                //et il attendra indéfiniment
                writer.flush();
                writer.close();
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
        try{
            String[] route =reponses.split(" ");
            String toSend;
            File file = new File("./src/main/resources"+route[1]);

            toSend = "HTTP/1.1 200 OK \r\n" +
                    "Date: "+  OffsetDateTime.now().toString() +"\r\n" +
                    "Server: Apache/1.3.12 (Unix) \r\n" +
                    "Last-Modified: "+ new Timestamp(file.lastModified()) +"\r\n" +
                    "Content-Length: "+ file.length() +"\r\n" +
                    "Connection: close \r\n" ;
            if(route[1].startsWith("/text")){

               try{
                   Scanner sc = new Scanner(file);
                   toSend += "Content-Type: text \r\n" +
                           "\r\n";

                   while (sc.hasNextLine())
                       toSend+=sc.nextLine()+"\r\n";

                   toSend +="\r\n";
               }catch(FileNotFoundException e){
                  return  send404Error(route[1]);
               }

            }else if(route[1].startsWith("/image")){
                try{
                toSend += "Content-Type: image \r\n" +
                        "\r\n";
                toSend +=encodeFileToBase64Binary(file);
                toSend +="\r\n";
                }catch(IOException e){
                    return  send404Error(route[1]);
                }

            }
                toSend+="\n\n";

            return toSend;
        }catch (Exception e){
            return send500Error();
        }

    }

    private String send500Error(){
       String  toSend="HTTP/1.1 500 Internal Server Error\r\n" +
                "Date: "+  OffsetDateTime.now().toString() +"\r\n" +
                "Server: Apache/2.2.14 (Win32) \r\n" +
                "Content-Length: 230 \r\n" +
                "Content-Type: text/html; charset=iso-8859-1 \r\n" +
                "Connection: Closed \r\n" +
                "\r\n";
        toSend+="!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                "<html><head>\n" +
                "<title>500 Internal Server Error</title>\n" +
                "</head><body>\n" +
                "<h1>Server Error</h1>\n" +
                "</body></html>";

    return toSend;
    }

    private String send404Error(String path){
        String toSend="HTTP/1.1 404 Not Found\r\n" +
                "Date: "+  OffsetDateTime.now().toString() +"\r\n" +
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
                "   <p>The requested URL "+path+"was not found on this server.</p>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        return toSend;
    }
    private static String encodeFileToBase64Binary(File file) throws IOException {
        String encodedfile = null;

            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.getMimeEncoder().encodeToString(bytes));

        return encodedfile;
    }
}
