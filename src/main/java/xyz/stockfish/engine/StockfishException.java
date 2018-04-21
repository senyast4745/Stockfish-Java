package xyz.stockfish.engine;

public class StockfishException extends RuntimeException {
    public StockfishException() {
        super();
    }

    public StockfishException(String message) {
        super(message);
    }

    public StockfishException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockfishException(Throwable cause) {
        super(cause);
    }

}
