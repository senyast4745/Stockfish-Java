package xyz.niflheim.utils;

public class Move {
    private final Query query;
    private final String fen;
    private final String move;
    private final int difficulty;

    public Move(Query query, String fen, String move, int difficulty) {
        this.query = query;
        this.fen = fen;
        this.move = move;
        this.difficulty = difficulty;
    }

    public Query getQuery() {
        return query;
    }

    public String getFen() {
        return fen;
    }

    public String getMove() {
        return move;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
