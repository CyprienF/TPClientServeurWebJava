package HTTPClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket socket;

    public Client() {

    }

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void runClient(String host, String path, int port) throws IOException {

        this.socket = new Socket(host, port);

        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        DataInputStream in = new DataInputStream(this.socket.getInputStream());

        String request = "GET " + path + " HTTP/1.1\r\n";
        request += "Host: "+ host + "\r\n";
        request += "\r\n";

        sendMessage(out, request);
        readResponse(in);

        out.close();
        in.close();
        this.closeSocket();
    }

    private static void sendMessage(DataOutputStream out, String request) throws IOException {
        System.out.println("* Request");
        System.out.println(request);

        out.write(request.getBytes());
        out.flush();
    }

    private static void readResponse(DataInputStream in) throws IOException {
        System.out.println("* Response");

        String content = "";
        while ((content = in.readLine()) != null) {
            System.out.println(content);
        }
    }

    private void closeSocket() {
        try {
            this.socket.close();
            System.out.println("The socket is closed");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
