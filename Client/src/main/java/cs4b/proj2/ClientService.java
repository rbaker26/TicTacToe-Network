package cs4b.proj2;

import javafx.application.Platform;
import javafx.util.Pair;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//************************************************************************************
public class ClientService implements Runnable{
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;
    BoardGUI bg;
    PlayerBehavior pb = null;
    int gameID;
    String name;
    char token;
    P_FLAGS flag;


    ArrayList<Pair<Integer,Integer>> moves = new ArrayList<>();

    @Override
    public void run() {

        // Player 1
        moves.add(new Pair<>(0,0));
        moves.add(new Pair<>(0,1));
//        moves.add(new Pair<>(0,2));
        moves.add(new Pair<>(2,0));
        moves.add(new Pair<>(2,1));



        /*
        x x o
        o o x
        x o x
         */

        // Player 2
//        moves.add(new Pair<>(0,2));
//        moves.add(new Pair<>(1,0));
//        moves.add(new Pair<>(1,1));
//        moves.add(new Pair<>(2,1));




        try {
            BoardWrapper bw;
            if(flag == P_FLAGS.CREATE) {
                sendInit(new InitWrapper(P_FLAGS.CREATE, this.name, this.token, 0));

            }
            else {
                sendInit(new InitWrapper(P_FLAGS.JOIN, this.name, this.token, this.gameID));
            }
                InitWrapper iw = getInit();

                //todo
                // store the token for p2 in the p2 client or else the p2 will not know what therir thing is

                System.out.println(iw.roomID);
                this.gameID = iw.roomID;
                iw = getInit();
                int index = 0;
            while (true) {

                bw = getBoardWrapper();
                System.out.println(bw.getBoard());


                if(bw.getFlag() == P_FLAGS.REQUEST_MV) {
                  //  System.out.println("Row:\t");
//                    if(sc.hasNext()) {
//                        row = sc.nextInt();
//                    }
                   // sc.next();
                  //  System.out.println("Col:\t");
                   // col = sc.nextInt();
                   // sendMove(new MoveWrapper(row,col,'x'));
                    if(pb == null) {
                        bg.drawBoard(bw.getBoard());
                        bg.turnOn();
                        //Pair<Integer,Integer> mov = moves.get(index);

                        Pair<Integer, Integer> mov = bg.getNextMove();
                        while (mov == null) {
                            mov = bg.getNextMove();
                            System.out.println("LOL");

                        }
                        System.out.println("Move Captureed");
                        int i = mov.getKey();
                        int j = mov.getValue();
                        sendMove(new MoveWrapper(i, j, this.token));
                    }
                    else {
                        PlayerBehavior.MoveInfo mi = pb.getMove(bw.getBoard(), this.token);
                        sendMove(new MoveWrapper(mi.getX(), mi.getY(), this.token));
                    }
                }
                else if(bw.getFlag() == P_FLAGS.ERROR_FATAL) {
                    System.out.println("bad move bruh");

                }
                else if(bw.getFlag() == P_FLAGS.TIE) {
                    System.out.println("TIE");
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.P1_WIN) {
                    System.out.println("Player 1 wins");
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.P2_WIN) {
                    System.out.println("Player 2 wins");
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.GAME_OVER) {
                    System.out.println("Game over, player disconnected");
                    break;
                }


//                bw= getBoardWrapper();
//                System.out.println(bw.getBoard().toString());
                System.out.println();
                System.out.println();
//                int i;
//                int j;
//                i = sc.nextInt();
//                sc.next();
//                j = sc.nextInt();
//                sendMove(new MoveWrapper(j,i,'x'));
//                bw= getBoardWrapper();
//
//                System.out.println(bw.getBoard().toString());

            }
        }
        catch (Exception e) {
            System.out.println("ERORRR");
            System.out.println(e);
            e.printStackTrace();

        }
    }

    //***************************************************************************
    public ClientService(IPAddress serverIP, PortWrapper port, BoardGUI bg, PlayerBehavior pb, String name, char token, int gameID, P_FLAGS flag) throws IOException{
        socket = new Socket(serverIP.toString(),port.getPort());

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
        this.bg = bg;
        this.pb = pb;
        this.name = name;
        this.token = token;
        this.gameID = gameID;
        this.flag = flag;
    }
    //***************************************************************************


    public int getID() {
        return gameID;
    }
    //***************************************************************************
    public void sendInit(String playerName, char token, P_FLAGS flag) throws IOException {
        sendInit(new InitWrapper(flag, playerName, token, 0));
    }
    //***************************************************************************

    //***************************************************************************

    public void sendInit(InitWrapper iw) throws IOException{
        os.writeObject(iw);
    }
    //***************************************************************************


    //***************************************************************************
    public InitWrapper getInit() throws IOException, ClassNotFoundException {
        return (InitWrapper) is.readObject();
    }
    //***************************************************************************

    //***************************************************************************
    public void sendMove(int col, int row, char token) throws IOException {
        sendMove(new MoveWrapper(col,row,token));
    }
    //***************************************************************************


    //***************************************************************************
    public void sendMove(MoveWrapper mp) throws IOException{
        os.writeObject(mp);
        os.flush();
        os.reset();
    }
    //***************************************************************************


    //***************************************************************************
    public BoardWrapper getBoardWrapper() throws IOException, ClassNotFoundException {
        BoardWrapper ret = (BoardWrapper) is.readUnshared();
      //  is.readUnshared();
        return ret;
    }
    //***************************************************************************
}
//************************************************************************************
