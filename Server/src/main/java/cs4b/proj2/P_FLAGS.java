package cs4b.proj2;

import java.io.Serializable;

enum P_FLAGS implements Serializable {
    CREATE,
    JOIN,
    P1_WIN,
    P2_WIN,
    TIE,
    REQUEST_MV,
    ERROR_FATAL,
    GAME_OVER,
    GAME_JOINED,
    GAME_FULL
}
