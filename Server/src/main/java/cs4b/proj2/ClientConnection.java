package cs4b.proj2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

//this class manages a client connection
class ClientConnection {
    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private char token;
    private String playerName;

    public ClientConnection(Socket s)
    {
        try
        {
            socket = s;
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public char getToken() {
        return token;
    }

    public String getName() {
        return playerName;
    }

    public MoveWrapper readMove() throws Exception {
        Object o = is.readObject();
        if(o instanceof MoveWrapper) {
            System.out.println("Move received: col" + ((MoveWrapper)o).col + " row " + ((MoveWrapper)o).row);
            return (MoveWrapper)o;
        }
        return null;
    }

    public InitWrapper readInit() throws Exception {
        Object o = is.readObject();
        if(o instanceof InitWrapper) {
            System.out.println("Instance of checks");
        }
        InitWrapper init = (InitWrapper)o;
        if(init.flag == P_FLAGS.CREATE) {
            this.token = init.token;
        }
        this.playerName = init.playerName;
        return init;
    }

    public void setToken(char token) {
        this.token = token;
    }

    public void writeInit(InitWrapper message) throws Exception {
        os.writeObject(message);
        os.flush();
    }

    public void write(BoardWrapper message) throws Exception {
        //os.writeUnshared(message);
        os.writeObject(message);
        os.flush();
        os.reset();
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, token, playerName);
    }
}