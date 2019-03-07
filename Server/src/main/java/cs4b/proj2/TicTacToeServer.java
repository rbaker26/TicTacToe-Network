import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeServer
{
  public static final int PORT = 5000;

  //Server manages a list of connections.
  List<ClientConnection> connections;

  public void startServer()
  {
      connections = new ArrayList<>();

      try
      {
        ServerSocket sSocket = new ServerSocket(PORT);

        while (true)
        {
          Socket client = sSocket.accept();
          ClientConnection cConn = new ClientConnection(client);
          connections.add(cConn);

          Thread t = new Thread(cConn);
          t.start();
          System.out.println("Connection made to client " + cConn.socket.getRemoteSocketAddress());
        }
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
  }

  public void broadcastMessage(Packet p)
  {
    Iterator<ClientConnection> it = connections.iterator();
    String enumType;
    switch(p.flag) {
    	case P1_WIN:
    	enumType = "P1 Win";
    	break;
    	case P2_WIN:
    	enumType = "P2 Win";
    	break;
    	case TIE:
    	enumType = "Tie";
    	break;
    	case NPC_REQUEST_MV:
    	enumType = "NPC Move";
    	break;
    	case UPDATE_BOARD:
    	enumType = "Update board";
    	break;
    	case ERROR_FATAL:
    	enumType = "Error";
    	break;
    	default:
    	enumType = "";
    	break;
    }

    while (it.hasNext())
    {
      try
      {
        PrintWriter writer = it.next().writer;
        writer.println(enumType);
        writer.flush();
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }

    }
  }


  //this class manages a client connection and also listens
  //for messages from the client (will be a separate thread per client)
  class ClientConnection implements Runnable
  {
    Packet p;
    Socket socket;
    PrintWriter writer;

    public ClientConnection(Socket s)
    {
      try
      {
        socket = s;
        ObjectInputStream isr = new ObjectInputStream(socket.getInputStream());
        p = (Packet) isr.readObject();

        writer = new PrintWriter(s.getOutputStream());

      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }

    public void run()
    {
      String message;
      try
      {
        while ((message = reader.readLine()) != null)
        {
          System.out.println("Received Message from " + socket.getRemoteSocketAddress() + "\n\t" + message);
          broadcastMessage(message);
        }
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }

public static void main(String[] args)
{
  TicTacToeServer server = new TicTacToeServer();
  server.startServer();
}

}

enum P_FLAGS {
	P1_WIN,
	P2_WIN,
	TIE,
	NPC_REQUEST_MV,
	UPDATE_BOARD,
	ERROR_FATAL
}

class Board implements Serializable {}
class Packet implements Serializable {
	public Board b;
	public P_FLAGS flag;
	public Packet(final P_FLAGS flag, final Board b) {
		this.flag = flag;
		this.b = b;
	}

}