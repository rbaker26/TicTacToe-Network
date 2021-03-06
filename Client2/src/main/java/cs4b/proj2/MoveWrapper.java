package cs4b.proj2;
import java.io.Serializable;

// joinMove and createMove packets should both be (-1,-1)
//************************************************************************************
public class MoveWrapper implements Serializable {

    private static final long serialVersionUID = 6529685098267757780L;
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