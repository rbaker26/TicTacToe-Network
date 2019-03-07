package Client;
import java.net.*;
import java.io.*;

//************************************************************************************
public class ClientService implements Runnable{
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream is;

    @Override
    public void run() {

    }

    //***************************************************************************
    public ClientService(IPAddress serverIP, PortWrapper port) throws IOException{
        socket = new Socket(serverIP.toString(),port.getPort());

        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());

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

