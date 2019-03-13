package Client;
import java.io.Serializable;

// joinMove and createMove packets should both be (-1,-1)
//************************************************************************************
public class MoveWrapper implements Serializable {

    //***************************************************************************
    public int row;
    public int col;
    public char token;
    //***************************************************************************


    //***************************************************************************
    MoveWrapper(int col, int row, char token) {
        this.row = row;
        this.col = col;
        this.token = token;
    }
    //***************************************************************************
}
//************************************************************************************