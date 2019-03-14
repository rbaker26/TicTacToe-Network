package cs4b.proj2;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;


/**
 * This board is a simple 3 x 3 grid set up to play tic-tac-toe using a javafx gridpane
 * The constructor iterates through each cell of the gridpane placing a pane with an ImageView.
 * Each pane is set up with an on click event to handle the toggling the tokens in the space
 */
public class BoardGUI extends GridPane {

    private final int MAX_SIZE = 3;
    private Image xImg;
    private Image oImg;
    private Image emptyImg;
    private boolean myTurn = true;
    Pair<Integer,Integer> nextMove;
    private boolean xTurn = true;
    private Image myToken;

    BoardGUI() {
        try {
            xImg = new Image(new FileInputStream("src/main/resources/cs4b/proj2/img/X.png"));
            oImg = new Image(new FileInputStream("src/main/resources/cs4b/proj2/img/O.png"));
            emptyImg = new Image(new FileInputStream("src/main/resources/cs4b/proj2/img/Empty.png"));
        }
        catch(Exception ex) {
            System.out.println("File not found");
        }

        for (int row = 0; row < MAX_SIZE; row++) {
            for (int col = 0; col < MAX_SIZE; col++) {
                Pane space = new Pane();
                ImageView token = new ImageView();
                token.setImage(emptyImg);
                token.setPreserveRatio(true);
                token.setFitHeight(100);
                space.getChildren().add(token);
                space.setOnMouseClicked(event -> {
                    System.out.println(event.getButton());
                    if(event.getButton() == MouseButton.SECONDARY) {
                        this.resetBoard();
                    } else {
                        int selectedCol = GridPane.getColumnIndex((Node)event.getSource());
                        int selectedRow = GridPane.getRowIndex((Node)event.getSource());

                        System.out.println(""+ selectedRow + "  " + selectedCol);



                        if(myTurn /*only do this step it is my turn and if it is valid move*/) {
                        System.out.println("HEREE");

                            if(token.getImage().equals(emptyImg)) {
                                //this is a valid move
                                nextMove =  new Pair<>(selectedRow, selectedCol);

                                System.out.println("HEREdsfadsfE");

                                token.setImage(myToken);
                                //TODO put code here
                                turnOff();
//                                nextMove = null;
                            }




                        }

                    }
                });
                this.add(space, col, row);
            }
        }
        this.setGridLinesVisible(true);
    }

    public void turnOn() {
        this.myTurn = true;
    }

    public void turnOff() {
        this.myTurn = false;
    }

    public void resetBoard() {
        ObservableList<Node> nodes = this.getChildren();
        ImageView image;
        for(Node node : nodes) {
            if(node instanceof Pane) {
                image = (ImageView) ((Pane) node).getChildren().get(0);
                image.setImage(emptyImg);
            }
        }
    }

    public Pair<Integer,Integer> getNextMove() {
        Pair<Integer, Integer> temp = this.nextMove;
        nextMove = null;
        return temp;
    }
    public void drawBoard(Board currentBoard) {
        //System.out.println("Drawing board");

        ObservableList<Node> nodes = this.getChildren();
        ImageView image;
        for(Node node : nodes) {

            if(node instanceof Pane) {
                image = (ImageView) ((Pane) node).getChildren().get(0);
                int columnIndex = GridPane.getColumnIndex(node);
                int rowIndex = GridPane.getRowIndex(node);

                switch(currentBoard.getPos(rowIndex, columnIndex)) {
                    case 'x':
                        image.setImage(xImg);
                        break;
                    case 'o':
                        image.setImage(oImg);
                        break;
                    case ' ':
                        image.setImage(emptyImg);
                        break;
                    default:
                        throw new RuntimeException("Invalid character in board");
                }
            }
        }

    }

    public void toggleTurn() {
        myTurn = !myTurn;
    }
    static public class Finished {
    }

    public void setMyToken(char token) {
        if(token == 'x') {
            this.myToken = xImg;
        }
        else {
            this.myToken = oImg;
        }
    }



}
