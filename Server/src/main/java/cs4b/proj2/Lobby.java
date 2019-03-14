package cs4b.proj2;

class Lobby implements Runnable {
    private ClientConnection player1 = null;
    private ClientConnection player2 = null;
    private int lobbyID;
    public boolean isAvailable = true;
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