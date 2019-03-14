package cs4b.proj2;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TicTacToeServer
{
    public static final int PORT = 6464;
    private ConcurrentHashMap<Integer, Lobby> rooms = new ConcurrentHashMap<>();
    private ServerSocket sSocket = null;

    public void startServer() {
        try
        {
            System.out.println("Top of loop");
            sSocket = new ServerSocket(PORT);
            System.out.println("Server socket opened");
            while (true) {
                Socket client = sSocket.accept();
                ClientConnection cConn = new ClientConnection(client);
                InitWrapper iw = cConn.readInit();
                switch(iw.flag) {
                    case CREATE:
                        Lobby lobby = new Lobby(cConn, cConn.hashCode());
                        rooms.put(cConn.hashCode(), lobby);
                        cConn.writeInit(new InitWrapper(P_FLAGS.CREATE, "", cConn.getToken(), cConn.hashCode()));
                        System.out.println("New lobby created");
                        break;
                    case JOIN:
                        System.out.println("Top of join");
                        if(rooms.get(iw.roomID).isAvailable) {
                            System.out.println("Joining lobby");
                            rooms.get(iw.roomID).addPlayer(cConn);
                            System.out.println("TEST: player joined: " + cConn.getName() + " token: " + cConn.getToken());
                            cConn.writeInit(new InitWrapper(P_FLAGS.GAME_JOINED, cConn.getName(), cConn.getToken(), iw.roomID));
                            rooms.get(iw.roomID).broadcastStart();
                            Thread lobbyThread = new Thread(rooms.get(iw.roomID));
                            lobbyThread.start();
                        } else {
                            System.out.println("Lobby full, rejecting player");
                            cConn.writeInit(new InitWrapper(P_FLAGS.GAME_FULL, "", cConn.getToken(), cConn.hashCode()));
                        }
                        break;
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }




    public static void main(String[] args)
    {
      TicTacToeServer server = new TicTacToeServer();
      System.out.println("Starting server.....");
      server.startServer();
    }

}
