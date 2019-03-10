
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
    }

    public static void main(String[] args) {
        launch(args);
    }

}
