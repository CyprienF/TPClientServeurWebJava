package HTTPclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;
    private static final String adresse = "127.0.0.1";
    private static final int port = 1026;
    private static final String request = "http://polytech.univ-lyon1.fr";

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void runClient() {
        try {
            socket = new Socket("fr.wikipedia.org", port);

            String request = "GET /wiki/Digital_Learning HTTP/1.1\r\n";

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeBytes(request);
            out.flush();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            String data = in.readLine();

            String content = "";
            int count = in.available();
            byte[] buffer = new byte[count];
            int stream;

            while((stream = in.read(buffer)) != -1){
                content += new String(buffer, 0, stream);
            }

            this.closeSocket();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("The socket is closed");
            System.out.println(e);
        }
    }
}
