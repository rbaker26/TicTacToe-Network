package Client;
import com.sun.glass.ui.Application;

import java.net.*;
import java.io.*;
import java.util.Scanner;

//************************************************************************************
public class ClientService implements Runnable{
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;

    @Override
    public void run() {

        try {
            Scanner sc = new Scanner(System.in);

            sendInit(new InitWrapper(P_FLAGS.CREATE, "bobby", 'x',0));
            InitWrapper iw = getInit();

            System.out.println(iw.roomID);
            while (true) {



                BoardWrapper bw = getBoardWrapper();

                int i;
                int j;
                i = sc.nextInt();
                sc.next();
                j = sc.nextInt();
                sendMove(new MoveWrapper(j,i,'x'));
                bw= getBoardWrapper();

                System.out.println(bw.getBoard().toString());

            }
        }
        catch (Exception e) {
            System.out.println("ERORRR");

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
        BoardWrapper ret = (BoardWrapper) is.readObject();
        return ret;
    }
    //***************************************************************************
}
//************************************************************************************

