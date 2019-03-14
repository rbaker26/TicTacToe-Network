package Client;

import javafx.event.EventHandler;
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

import java.util.Random;

public class Controller {

    private RadioButton xButton;
    private RadioButton oButton;
    private TextField nameField;
    private NumberTextField roomNumberField;

    private String getName() {
        return nameField.getText();
    }

    private char getSymbol() {
        if(xButton.selectedProperty().get()) {
            return 'X';
        }
        else if(oButton.selectedProperty().get()) {
            return 'O';
        }
        else {
            throw new RuntimeException("No symbol selected");
        }
    }

    private String makeRandomName() {
        final String[] names = {"Waldo", "Grandmaster" , "Steven", "Goku", "Yugi", "Ash", "Sean", "John", "Herbert"};
        return names[new Random().nextInt(names.length)];
    }


    //If the player wants to compete against another player
    //button1Click() will be incorporated.


    public void showRoomSetupDialog(Scene origScene, EventHandler<ActionEvent> submitAction) {

        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(25, 25, 25, 25));


        nameField = new TextField();
        nameField.setText(makeRandomName());

        xButton = new RadioButton("X");
        oButton = new RadioButton("O");

        final ToggleGroup symbolGroup = new ToggleGroup();
        xButton.setToggleGroup(symbolGroup);
        oButton.setToggleGroup(symbolGroup);

        HBox symbolBox = new HBox(10);
        symbolBox.getChildren().addAll(xButton, oButton);

        // This will randomly select whether we are going to
        // be X or O. The user can still change it, of course.
        if((int)(Math.random() * 2) == 0) {
            xButton.selectedProperty().setValue(true);
        }
        else {
            oButton.selectedProperty().setValue(true);
        }

        Button submitButton = new Button("Submit");
        submitButton.defaultButtonProperty().setValue(true);
        submitButton.setOnAction(submitAction);

        Button cancelButton = new Button("Cancel");
        cancelButton.cancelButtonProperty().setValue(true);
        cancelButton.setOnAction((ActionEvent event) ->
                App.getPrimaryStage().setScene(origScene)
        );


        mainPane.add(new Label("Enter your name: "), 0, 1);
        mainPane.add(nameField, 1, 1);
        mainPane.add(new Label("Choose your token:"), 0, 2);
        mainPane.add(symbolBox, 1, 2);

        mainPane.add(cancelButton, 0, 3);
        mainPane.add(submitButton, 1, 3);



        //goes to the very bottom:
        Scene scene = new Scene(mainPane, 500, 300);

        // mainPane.setResizable(false);
        App.getPrimaryStage().setScene(scene);
        App.getPrimaryStage().show();
    }

    public void showClientDialog(Scene origScene, EventHandler<ActionEvent> submitAction) {

        GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(25, 25, 25, 25));


        nameField = new TextField();
        nameField.setText(makeRandomName());

        roomNumberField = new NumberTextField();

        Button submitButton = new Button("Submit");
        submitButton.defaultButtonProperty().setValue(true);
        submitButton.setOnAction(submitAction);

        Button cancelButton = new Button("Cancel");
        cancelButton.cancelButtonProperty().setValue(true);
        cancelButton.setOnAction((ActionEvent event) ->
                App.getPrimaryStage().setScene(origScene)
        );

        mainPane.add(new Label("Enter your name: "), 0, 1);
        mainPane.add(nameField, 1, 1);
        mainPane.add(new Label("Room number:"), 0, 2);
        mainPane.add(roomNumberField, 1, 2);

        mainPane.add(cancelButton, 0, 3);
        mainPane.add(submitButton, 1, 3);



        //goes to the very bottom:
        Scene scene = new Scene(mainPane, 500, 300);

        // mainPane.setResizable(false);
        App.getPrimaryStage().setScene(scene);
        App.getPrimaryStage().show();
    }

    //When Create Room is clicked button1Click() is called.
    @FXML
    private void button1Click() {

        System.out.println("Button 1 was clicked");

        //multiplayer - prompt a window that asks for IP address from user.
        //Call Socket class object to pass in IP address and port 6464.


        //int port = 6464;

        showRoomSetupDialog(
                App.getPrimaryStage().getScene(),
                (ActionEvent event) -> {
                    System.out.println("My name is " + getName());
                    System.out.println("My symbol is " + getSymbol());
                });



    }

    //If the player wants to compete against the AI in an easy level,
    //button2Click() will be incorporated.
    @FXML
    private void button2Click() {

        System.out.println("Button 2 was clicked");


        showRoomSetupDialog(
                App.getPrimaryStage().getScene(),
                (ActionEvent event) -> {
                    System.out.println("My name is " + getName());
                    System.out.println("My symbol is " + getSymbol());
                });
    }

    //If the player wants to compete against the AI in a hard level,
    //button3Click() will be incorporated.
    @FXML
    private void button3Click() {

        System.out.println("Button 3 was clicked");

        showRoomSetupDialog(
                App.getPrimaryStage().getScene(),
                (ActionEvent event) -> {
                    System.out.println("My name is " + getName());
                    System.out.println("My symbol is " + getSymbol());
                });
    }

    @FXML
    private void button4Click() {

        System.out.println("Button 4 was clicked");

        showClientDialog(
                App.getPrimaryStage().getScene(),
                (ActionEvent event) -> {
                    try {
                        int roomNumber = Integer.parseInt(roomNumberField.getText());

                        System.out.println("My name is " + getName());
                        System.out.println("The room is " + roomNumber);
                    } catch(NumberFormatException exception) {
                        System.out.println("Invalid room number");
                    }

                });
    }

}