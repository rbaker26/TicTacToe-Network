package cs4b.proj2;

import java.io.Serializable;

public class InitWrapper implements Serializable {
    public P_FLAGS flag;
    public String playerName;
    public char token;
    public int roomID;

    public InitWrapper(P_FLAGS flag, String playerName, char token, int roomID) {
        this.flag = flag;
        this.playerName = playerName;
        this.token = token;
        this.roomID = roomID;
    }
}
