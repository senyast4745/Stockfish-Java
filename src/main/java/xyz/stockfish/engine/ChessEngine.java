package xyz.stockfish.engine;

import java.io.Closeable;
import java.util.List;

public interface ChessEngine extends Closeable {
    List<String> getCheckers();

    List<String> getCheckers(String fen);

    String getBestMove(int difficulty);

    String getBestMove(String fen, int difficulty);

    void setFen(String fen);

    String getFen();

    String makeMove(String pgn);

    String makeMove(String fen, String pgn);
}
