package cs4b.proj2;

import javafx.util.Pair;

import java.util.Random;
import java.util.ArrayList;

/**
 * This is a stupid NPC which just selects a space randomly. Garunteed to
 * pick an empty space, though.
 *
 * @author Daniel Edwards
 */
public class NPCEasy implements PlayerBehavior {

    public NPCEasy() {
    }

    @Override
    public MoveInfo getMove(Board b, char token) {

        Pair<Integer, Integer> result = null;
        char[][] boardArray = b.getBoardArray();

        ArrayList<Pair<Integer, Integer>> emptySpaces = new ArrayList<>();

        // Get all empty spaces
        for(int x = 0; x < b.ROW_COUNT; x++) {
            for(int y = 0; y < b.COL_COUNT; y++) {
                if(b.getPos(x, y) == b.DEFAULT_VALUE) {
                    emptySpaces.add(new Pair<>(x, y));
                }
            }
        }

        // Select one of the spaces
        if(emptySpaces.size() > 0) {
            Random r = new Random();

            result = emptySpaces.get(r.nextInt(emptySpaces.size()));
        }
        else {
            throw new IllegalStateException("If we hit this, there is a big problem!!!");
            // This state should not be possible.
        }

        //b.setPos(result.getKey(),result.getValue(),token);
        return new MoveInfo(result.getKey(), result.getValue());
    }


}
