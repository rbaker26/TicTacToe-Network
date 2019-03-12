package cs4b.proj2;
import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeServer
{
  public static final int PORT = 6464;

  //Server manages a list of connections.
  List<Lobby> rooms;

      public void startServer()
{
    try
    {
        ServerSocket sSocket = new ServerSocket(PORT);

        while (true)
        {
            LobbyManager lm = new LobbyManager(sSocket);

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
            case CREATE:
                enumType = "Create";
                break;
            case JOIN:
                enumType = "Join";
                break;
            case P1_WIN:
                enumType = "P1 Win";
                break;
            case P2_WIN:
                enumType = "P2 Win";
                break;
            case TIE:
                enumType = "Tie";
                break;
            case REQUEST_MV:
                enumType = "NPC Move";
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

      class LobbyManager implements Runnable {
          ServerSocket sSocket;


          public LobbyManager(ServerSocket sSocket) {
              this.sSocket = sSocket;
          }

          public void run() {
              try {
                  while (true) {
                      Socket client = sSocket.accept();


                      ClientConnection cConn = new ClientConnection(client);


                      LobbyManager lm = new LobbyManager();
                      Thread lobbyListener = new Thread(lm);
                      lobbyListener.start();


                      rooms.add(cConn);

                      Thread t = new Thread(cConn);
                      t.start();
                      System.out.println("Connection made to client " + cConn.socket.getRemoteSocketAddress());
                  }
              }
              catch(Exception ex) {
                  ex.printStackTrace();
              }
          }

      }
      class Lobby implements Runnable {
          private ClientConnection player1;
          private ClientConnection player2;
          private String lobbyID;
          private boolean isAvailable = true;
          private boolean player1turn = true;


          public Lobby(ClientConnection player1, String lobbyID) {
              this.player1 = player1;
              this.lobbyID = lobbyID;
          }


      }
  //this class manages a client connection
      class ClientConnection {
          private Socket socket;
          private ObjectOutputStream os;
          private ObjectInputStream is;

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

          public MoveWrapper read() throws Exception {
              return (MoveWrapper) is.readObject();
          }

          public void write(BoardWrapper message) throws Exception {
              os.writeObject(message);
          }
      }

    public static void main(String[] args)
    {
      TicTacToeServer server = new TicTacToeServer();
      server.startServer();
    }

    enum P_FLAGS {
        CREATE,
        JOIN,
        P1_WIN,
        P2_WIN,
        TIE,
        REQUEST_MV,
        ERROR_FATAL
    }
}
