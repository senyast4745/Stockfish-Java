package xyz.niflheim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.generator.BoardGenerator;
import xyz.niflheim.utils.Move;
import xyz.niflheim.utils.Variant;
import xyz.stockfish.ChessEngine;
import xyz.stockfish.engine.StockfishException;
import xyz.stockfish.engine.StockfishInitException;
import xyz.stockfish.engine.StockfishPool;
import xyz.stockfish.utils.Option;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StockfishClient {
    private final Logger Log = LoggerFactory.getLogger(StockfishClient.class);
    private final BoardGenerator generator;
    private final StockfishPool pool;

    public StockfishClient(Variant variant, int instances, Option... options) {
        try {
            pool = new StockfishPool(variant, instances, options);
            generator = new BoardGenerator();
        } catch (StockfishInitException e) {
            throw new StockfishException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public synchronized String getBestMove(ChessEngine stockfish, String fen, int difficulty) {
        return stockfish.getBestMove(fen, difficulty);
    }

    public synchronized List<String> getCheckers(ChessEngine stockfish, String fen) {
        return stockfish.getCheckers(fen);
    }

    public synchronized String makeMove(ChessEngine stockfish, String fen, String move) {
        return stockfish.makeMove(fen, move);
    }

    public synchronized void close() {
        pool.close();
    }

    public void submit(Move move) {
        pool.submit(engine -> {
                    try {
                        switch (move.getQuery()) {
                            case CLOSE:
                                close();
                                break;
                            case BESTMOVE:
                                getBestMove(engine, move.getFen(), move.getDifficulty());
                                break;
                            case CHECKERS:
                                getCheckers(engine, move.getFen());
                                break;
                            case MAKEMOVE:
                                makeMove(engine, move.getFen(), move.getMove());
                                break;
                        }
                    } catch (Exception e) {
                        Log.error(e.getMessage());
                    }
                }
        );
    }

    public RenderedImage generateBoard(String fen) {
        return generator.generateBoard(fen);
    }

    public InputStream streamBoard(String fen) {
        try {
            return generator.generateBoardInputStream(fen);
        } catch (IOException e) {
            Log.error(e.getMessage());
        }

        return null;
    }

    public static class Builder {
        private Set<Option> options = new HashSet<>();
        private Variant variant;
        private int instances;

        public final Builder setInstances(int instances) {
            this.instances = instances;
            return this;
        }

        public final Builder setVariant(Variant variant) {
            this.variant = variant;
            return this;
        }

        public final Builder setOption(String name, int value) {
            options.add(new Option(name, value));
            return this;
        }

        public final StockfishClient build() {
            return new StockfishClient(variant, instances, (Option[]) options.toArray());
        }
    }
}
