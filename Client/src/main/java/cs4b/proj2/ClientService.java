package cs4b.proj2;

import java.net.*;
import java.io.*;
import java.util.Scanner;

//************************************************************************************
public class ClientService implements Runnable{
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;
    Scanner sc = new Scanner(System.in);
    @Override
    public void run() {

        try {

            sendInit(new InitWrapper(P_FLAGS.CREATE, "bobby", 'x',0));
            InitWrapper iw = getInit();

            //todo
            // store the token for p2 in the p2 client or else the p2 will not know what therir thing is

            System.out.println(iw.roomID);
            iw = getInit();
            System.out.print("Player2:\t");
            System.out.println(iw.playerName +" " +  iw.token);
            BoardWrapper bw;
            while (true) {

                bw = getBoardWrapper();
                System.out.println(bw.getBoard());

                int row = 0;
                int col = 0;
                if(bw.getFlag() == P_FLAGS.REQUEST_MV) {
                    System.out.println("Row:\t");
                    if(sc.hasNext()) {
                        row = sc.nextInt();
                    }
                   // sc.next();
                    System.out.println("Col:\t");
                   // col = sc.nextInt();
                   // sendMove(new MoveWrapper(row,col,'x'));
                    sendMove(new MoveWrapper(col,row++,'x'));
                }
                else if(bw.getFlag() == P_FLAGS.ERROR_FATAL) {
                    System.out.println("bad move bruh");

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
    public ClientService(IPAddress serverIP, PortWrapper port) throws IOException{
        socket = new Socket(serverIP.toString(),port.getPort());

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());

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

