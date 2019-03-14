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
                        if(!rooms.containsKey(iw.roomID)) {
                            cConn.writeInit(new InitWrapper(P_FLAGS.ERROR_FATAL, cConn.getName(), cConn.getToken(), iw.roomID));
                        }
                        else if(rooms.get(iw.roomID).isAvailable) {
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
            System.out.println("board: " + board);
            currentPlayer = player1;
            while(!gameOver) {
                MoveWrapper move;
                try {
                    do {
                        System.out.println("SENDING MOVE REQUEST WITH BOARD:");
                        System.out.println(board);
//                        ObjectOutputStream testOS = new ObjectOutputStream(System.out);
//                        testOS.writeObject(board);
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
                    System.out.println("Move received: row: " + move.row + " col: " + move.col + " token: " + move.token);
                    board.setPos(move.row, move.col, move.token);
                    System.out.println("Test: board after board.setPos:");
                    System.out.println(board);
                    if(isWon(currentPlayer.getToken())) {
                        if(currentPlayer.getToken() == 'x') {
                            broadcast(new BoardWrapper(P_FLAGS.P1_WIN, board));
                            gameOver = true;
                        } else {
                            broadcast(new BoardWrapper(P_FLAGS.P2_WIN, board));
                            gameOver = true;
                        }
                    }
                    else if(isFull()) {
                        broadcast(new BoardWrapper(P_FLAGS.TIE, board));
                        gameOver = true;
                    }
                    else if(player1turn) {
                        currentPlayer = player2;
                        player1turn = false;
                    } else {
                        currentPlayer = player1;
                        player1turn = true;
                    }
                }
                catch(SocketException ex) {
                    System.out.println("Socket exception caught!");
                    gameOver = true;
                    try {
                        player1.write(new BoardWrapper(P_FLAGS.GAME_OVER, board));
                    }
                    catch(Exception ex2) {
                        System.out.println("lols");
                    }
                    try {
                        player2.write(new BoardWrapper(P_FLAGS.GAME_OVER, board));
                    }
                    catch(Exception ex2) {
                        System.out.println("lols");
                    }
                }
                catch(EOFException ex) {
                    System.out.println("EOF Exception caught!");
                    gameOver = true;
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
        public void broadcastStart() {
            try {
                player1.writeInit(new InitWrapper(P_FLAGS.GAME_JOINED, player2.getName(), player2.getToken(), this.lobbyID));
                player2.writeInit(new InitWrapper(P_FLAGS.GAME_JOINED, player1.getName(), player1.getToken(), this.lobbyID));
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
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
            try {
                MoveWrapper move = (MoveWrapper)is.readObject();
                return move;
            }
            catch(Exception ex) {
                return null;
            }
        }

        public InitWrapper readInit() {
            try{
                InitWrapper init = (InitWrapper)is.readObject();
                if(init.flag == P_FLAGS.CREATE) {
                    this.token = init.token;
                }
                this.playerName = init.playerName;
                return init;
            }
            catch(Exception ex){
                return null;
            }
        }

        public void setToken(char token) {
            this.token = token;
        }

        public void writeInit(InitWrapper message) throws Exception {
            os.writeObject(message);
            os.flush();
            os.reset();
        }

        public void write(BoardWrapper message) throws Exception {
            os.writeObject(message);
            os.flush();
            os.reset();
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
