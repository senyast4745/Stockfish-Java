package xyz.stockfish;

import java.io.Closeable;
import java.util.function.Consumer;

public interface ChessEnginePool extends Closeable {
    void submit(Consumer<ChessEngine> consumer);
}
