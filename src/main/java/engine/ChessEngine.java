package engine;

public interface ChessEngine {
    boolean start();

    void stop();

    String reset();

    String getBestMove(String fen, int difficulty);

    String getCheckers(String fen);

    String getLegalMoves(String fen);

    String getState(String fen);

    String makeMove(String fen, String pgn);
}
