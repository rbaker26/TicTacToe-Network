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
    Scanner sc = new Scanner(System.in);
    BoardGUI bg;

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

            sendInit(new InitWrapper(P_FLAGS.JOIN, "keane", ' ',1319988438));

            InitWrapper iw = getInit();
            bg.setMyToken(iw.token);
            //todo
            // store the token for p2 in the p2 client or else the p2 will not know what therir thing is

            System.out.println(iw.roomID);
            iw = getInit();
            System.out.print("Player2:\t");
            System.out.println(iw.playerName +" " +  iw.token);
            BoardWrapper bw;
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
                    bg.toggleTurn();
                    bg.drawBoard(bw.getBoard());
                    //Pair<Integer,Integer> mov = moves.get(index);
                    Pair<Integer,Integer> mov = bg.getNextMove();
                    while(mov == null) {
                        mov = bg.getNextMove();
                        System.out.println("LOL");
                    }
                    int i = mov.getKey();
                    int j = mov.getValue();
                    sendMove(new MoveWrapper(i,j,'o'));
                    index++;
                }
                else if(bw.getFlag() == P_FLAGS.ERROR_FATAL) {
                    System.out.println("bad move bruh");

                }
                else if(bw.getFlag() == P_FLAGS.TIE) {
                    System.out.println("TIE");
                    bg.drawBoard(bw.getBoard());
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.P1_WIN) {
                    System.out.println("Player 1 wins");
                    bg.drawBoard(bw.getBoard());
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.P2_WIN) {
                    System.out.println("Player 2 wins");
                    bg.drawBoard(bw.getBoard());
                    break;
                }
                else if(bw.getFlag() == P_FLAGS.GAME_OVER) {
                    System.out.println("Game over, player disconnected");
                    bg.drawBoard(bw.getBoard());
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
    public ClientService(IPAddress serverIP, PortWrapper port, BoardGUI bg) throws IOException{
        socket = new Socket(serverIP.toString(),port.getPort());

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
        this.bg = bg;

    }
    //***************************************************************************



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

