package sample;

import HTTPClient.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private Client client;

    @Override
    public void start(Stage primaryStage) {
        // Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        // Host Label
        Label labelHost = new Label("Host : ");
        GridPane.setConstraints(labelHost, 0, 0);
        grid.getChildren().add(labelHost);

        // Host TextField
        final TextField hostTextfield = new TextField();
        GridPane.setConstraints(hostTextfield, 1, 0);
        grid.getChildren().add(hostTextfield);

        // Path Label
        Label pathLabel = new Label("Chemin : ");
        GridPane.setConstraints(pathLabel, 0, 1);
        grid.getChildren().add(pathLabel);

        // Path TextField
        final TextField pathTextfield = new TextField();
        GridPane.setConstraints(pathTextfield, 1, 1);
        grid.getChildren().add(pathTextfield);

        // Port Label
        Label labelPort = new Label("Port : ");
        GridPane.setConstraints(labelPort, 0, 2);
        grid.getChildren().add(labelPort);

        // Port TextField
        final TextField portTextfield = new TextField();
        GridPane.setConstraints(portTextfield, 1, 2);
        grid.getChildren().add(portTextfield);

        // Chercher Button
        Button chercher = new Button("Chercher");
        GridPane.setConstraints(chercher, 0, 3);
        grid.getChildren().add(chercher);

        // Webview
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        URL url = this.getClass().getResource("/toto.html");
        engine.load(url.toString());

        // Charger du texte brut
        //engine.loadContent("Salut");

        GridPane.setConstraints(webView, 2, 4);
        grid.getChildren().add(webView);

        chercher.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        client = new Client();
                        try {
                            client.runClient(hostTextfield.getText(), pathTextfield.getText(), Integer.parseInt(portTextfield.getText()));

                            URL content = new URL("http://" + hostTextfield.getText() + ":" + Integer.parseInt(portTextfield.getText()) + "" + pathTextfield.getText());
                            engine.load(content.toString());
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                    }
                });

        // Groupe des éléments de la scène
        Group group = new Group();
        group.getChildren().add(grid);

        // Affichage de la scène
        primaryStage.setScene(new Scene(group));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
