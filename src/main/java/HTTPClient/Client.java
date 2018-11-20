package HTTPClient;

import javafx.scene.web.WebEngine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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
        String response= readResponse(in);

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

    private static String readResponse(DataInputStream in) throws IOException {
        System.out.println("* Response");

        String content = "";
        String tmp="";

        while ((tmp = in.readLine()) != null) {
            System.out.println(tmp);
            content+=tmp;
        }
        return content;
    }

    private String getBody(String s){
        String body = "";
        String tmp="";
        Scanner sc = new Scanner(s);
        boolean bodyFound=false;
        while (sc.hasNextLine()){
            tmp=sc.nextLine();
            System.out.println(tmp);
            if(tmp.startsWith("\r\n")){
                bodyFound=!bodyFound;
            }
            if(bodyFound){
                body+=tmp;
            }
        }
        return body;
    }

    private static void readImage() {

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
