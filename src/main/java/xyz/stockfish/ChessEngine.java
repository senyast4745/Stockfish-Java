package xyz.stockfish;

import java.io.Closeable;
import java.util.List;

public interface ChessEngine extends Closeable {
    List<String> getCheckers(String fen);

    String getBestMove(String fen, int difficulty);

    String getFen();

    String makeMove(String fen, String pgn);

    boolean isAlive();
}
