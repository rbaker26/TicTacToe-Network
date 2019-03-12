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
            sSocket = new ServerSocket(PORT);
            System.out.println("Server socket opened");
            while (true) {
                Socket client = sSocket.accept();
                ClientConnection cConn = new ClientConnection(client);
                switch(cConn.readInit().flag) {
                    case CREATE:
                        Lobby lobby = new Lobby(cConn, cConn.hashCode());
                        rooms.put(cConn.hashCode(), lobby);
                        cConn.writeInit(new InitWrapper(P_FLAGS.CREATE, "", cConn.getToken(), cConn.hashCode()));
                        System.out.println("New lobby created");
                        break;
                    case JOIN:
                        if(rooms.get(cConn.readInit().roomID).isAvailable) {
                            System.out.println("Joining lobby");
                            rooms.get(cConn.readInit().roomID).addPlayer(cConn);
                            cConn.writeInit(new InitWrapper(P_FLAGS.GAME_JOINED, "", cConn.getToken(), cConn.hashCode()));
                            Thread lobbyThread = new Thread(rooms.get(cConn.readInit().roomID));
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

    class Lobby implements Runnable {
        private ClientConnection player1 = null;
        private ClientConnection player2 = null;
        private int lobbyID;
        private boolean isAvailable = true;
        private boolean gameOver = false;
        private boolean player1turn = true;
        private ClientConnection currentPlayer;
        private Board board = new Board();


        public Lobby(ClientConnection player, int lobbyID) {
            if(player.getToken() == 'x') {
                this.player1 = player;
            } else {
                this.player2 = player;
            }
            this.lobbyID = lobbyID;
        }

        public void run() {
            currentPlayer = player1;
            while(!gameOver) {
                MoveWrapper move;
                try {
                    do {
                        currentPlayer.write(new BoardWrapper(P_FLAGS.REQUEST_MV, board));
                        move = currentPlayer.readMove();
                        if(move == null) {
                            if(player1turn) {
                                player2.write(new BoardWrapper(P_FLAGS.GAME_OVER, board));
                            } else {
                                player1.write(new BoardWrapper(P_FLAGS.GAME_OVER, board));
                            }
                        }
                    } while(!isValid(move.row, move.col));
                    board.setPos(move.row, move.col, move.token);
                    if(isWon(currentPlayer.getToken())) {
                        if(currentPlayer.getToken() == 'x') {
                            broadcast(new BoardWrapper(P_FLAGS.P1_WIN, board));
                        } else {
                            broadcast(new BoardWrapper(P_FLAGS.P2_WIN, board));
                        }
                    }
                    else if(isFull()) {
                        broadcast(new BoardWrapper(P_FLAGS.TIE, board));
                    }
                    else if(player1turn) {
                        currentPlayer = player2;
                        player1turn = false;
                    } else {
                        currentPlayer = player1;
                        player1turn = true;
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void broadcast(BoardWrapper message) {
            try {
                player1.write(message);
                player2.write(message);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        public boolean isFull() {
            if(board.numEmptySpaces() == 0) {
                return true;
            }
            return false;
        }
        public boolean isValid(int row, int col) {
            if(board.getPos(row, col) == ' ') {
                return true;
            }
            return false;
        }
        public boolean isWon(char token) {
            char[][] cell = board.getBoardArray();
            // Check all rows
            for (int i = 0; i < 3; i++)
                if ((cell[i][0] == token)
                        && (cell[i][1] == token)
                        && (cell[i][2] == token)) {
                    gameOver = true;
                    return true;
                }

            /** Check all columns */
            for (int j = 0; j < 3; j++)
                if ((cell[0][j] == token)
                        && (cell[1][j] == token)
                        && (cell[2][j] == token)) {
                    gameOver = true;
                    return true;
                }

            /** Check major diagonal */
            if ((cell[0][0] == token)
                    && (cell[1][1] == token)
                    && (cell[2][2] == token)) {
                gameOver = true;
                return true;
            }

            /** Check subdiagonal */
            if ((cell[0][2] == token)
                    && (cell[1][1] == token)
                    && (cell[2][0] == token)) {
                gameOver = true;
                return true;
            }

            /** All checked, but no winner */
            return false;
        }
        public void addPlayer(ClientConnection player) {
            if(player1 == null) {
                this.player1 = player;
                player.setToken('x');
            } else {
                this.player2 = player;
                player.setToken('o');
            }
            this.isAvailable = false;
        }
    }
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
              if(is.readObject() instanceof MoveWrapper) {
                  return (MoveWrapper) is.readObject();
              }
              return null;
          }

          public InitWrapper readInit() throws Exception {
              InitWrapper init = (InitWrapper) is.readObject();
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
          }

          public void write(BoardWrapper message) throws Exception {
              os.writeObject(message);
          }

          @Override
          public int hashCode() {
            return Objects.hash(socket, token, playerName);
          }
      }

    public static void main(String[] args)
    {
      TicTacToeServer server = new TicTacToeServer();
      System.out.println("Starting server.....");
      server.startServer();
    }

}
