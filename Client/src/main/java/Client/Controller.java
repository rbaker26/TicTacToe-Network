package Client;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {

    public static Controller getInstance() {
        //System.out.println(instance);
        return instance;
    }
    private static Controller instance;


    //On Mouse Click Options:

    //If the player wants to compete against another player
    //button1Click() will be incorporated.

    public Controller() {
        instance = this;
    }



    @FXML
    private TextField player1txt;

    @FXML
    private TextField player2txt;


    public String getPlayer1() {
        String player1 = player1txt.getText();

        if(player1 == "") {
            player1 = "Player 1";
        }

        return player1;
    }

    public String getPlayer2() {
        String player2 = player2txt.getText();

        if(player2 == "") {
            player2 = "Player 2";
        }

        return player2;
    }

    @FXML
    private void button1Click() {

        System.out.println("Button 1 was clicked");

        //multiplayer - prompt a window that asks for IP address from user.
        //Call Socket class object to pass in IP address and port 6464.


        int port = 6464;


        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(25, 25, 25,25));

        Label playerName = new Label("Enter your name: ");
        mainPane.add(playerName, 0, 1);

        TextField typeInName = new TextField();
        mainPane.add(typeInName, 1, 1);



        //goes to the very bottom:
        Scene scene = new Scene(mainPane, 300, 300);
        App.getPrimaryStage().setScene(scene);
        App.getPrimaryStage().show();


    }

    //If the player wants to compete against the AI in an easy level,
    //button2Click() will be incorporated.
    @FXML
    private void button2Click() {

        System.out.println("Button 2 was clicked");


    }

    //If the player wants to compete against the AI in a hard level,
    //button3Click() will be incorporated.
    @FXML
    private void button3Click() {

        System.out.println("Button 3 was clicked");

    }



}