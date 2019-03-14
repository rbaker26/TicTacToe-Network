
package cs4b.proj2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;

    public String getGreeting() {
        return "Hello world.";
    }


    public  void start(Stage primaryStage) throws Exception {

        BoardGUI board = new BoardGUI();
        try{
            // ClientService cs = new ClientService(new IPAddress("10.0.0.30"), new PortWrapper(6464) );

            String ipAddress = "127.0.1.1";
            System.out.println("Connecting to " + ipAddress);
            ClientService cs = new ClientService(new IPAddress(ipAddress), new PortWrapper(6464), board );
            Thread th = new Thread(cs);
            th.start();
        }
        catch(IOException ioe) {
            System.out.println(ioe);
            System.out.println("AHHAHAHAHA");
        }


        primaryStage.setTitle("Hello World");
        board.requestFocus();
        primaryStage.setScene(new Scene(board, 300, 300));
        primaryStage.show();

        System.out.println(new App().getGreeting());

        Board b = new Board();
       // b.setPos(2, 1, 'O');
        board.drawBoard(b);
    }

    public static void main(String[] args) {
        launch(args);
//        Stage primaryStage;
//        Application.start(primaryStage);

    }

    public void setPrimaryStage(Stage primaryStage) {
        App.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage(){
        return primaryStage;
    }
}

/*
package Client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public String getGreeting() {
        return "Hello world.";
    }

    public void start(Stage primaryStage) throws Exception {
        BoardGUI board = new BoardGUI();
        primaryStage.setTitle("Hello World");
        board.requestFocus();
        primaryStage.setScene(new Scene(board, 300, 300));
        primaryStage.show();

        System.out.println(new App().getGreeting());

        Board b = new Board();
        b.setPos(2, 1, 'O');
        board.drawBoard(b);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
*/

/*
package Client;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.io.File;


public class App extends Application {

    //Observer Signal- main captures, then it will call the board ui.

    Parent root;
    Scene startScene;
    Scene boardScene;
    BoardGUI board;
    File file = new File("gameState");

    //Controller controlObject;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        this.primaryStage = primaryStage;

        startScene = new Scene(root, 360, 450);


        primaryStage.setResizable(true);

        primaryStage.setScene(startScene);

        primaryStage.show();

    }

    public void setPrimaryStage(Stage primaryStage) {
        App.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage(){
        return primaryStage;
    }
}

*/