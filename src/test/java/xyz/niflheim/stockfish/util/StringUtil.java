package xyz.niflheim.stockfish.util;

public class StringUtil {
    public static final String START_REGEX = "^";
    public static final String GREETING_STOCKFISH =
            "Stockfish 10 64 by T. Romstad, M. Costalba, J. Kiiski, G. Linscott";
    public static final String ERROR_STOCKFISH = "Unknown command: ";
    public static final String FEN_HEADER = "(position fen )";
    public static final String FEN_REGEX = "(([rnbqkp1-8PRNBQK]{1,8}/){7}[rnbqkp1-8PRNBQK]{1,8})(\\s)([wb])" +
            "(\\s[\\-kqKQ]{1,4}\\s)((-)|[a-h][1-8])(\\s)([0-9]+)(\\s)([0-9]+)";
    public static final String MOVE_REGEX = "([a-h][1-8]){2}";
    public static final String END_REGEX = "$";
    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
}
