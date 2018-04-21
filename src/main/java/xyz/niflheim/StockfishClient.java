package xyz.niflheim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.generator.BoardGenerator;
import xyz.niflheim.utils.Variant;
import xyz.stockfish.engine.ChessEngine;
import xyz.stockfish.Stockfish;
import xyz.stockfish.engine.StockfishException;
import xyz.stockfish.engine.StockfishInitException;
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
    private final Stockfish stockfish;

    public StockfishClient(Variant variant, Option... options) {
        try {
            stockfish = new Stockfish(variant, options);
            generator = new BoardGenerator();
        } catch (StockfishInitException e) {
            throw new StockfishException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getFen() {
        return stockfish.getFen();
    }

    public String getBestMove(String fen, int difficulty) {
        return stockfish.getBestMove(fen, difficulty);
    }

    public List<String> getCheckers(String fen) {
        return stockfish.getCheckers(fen);
    }

    public String makeMove(String fen, String move) {
        return stockfish.makeMove(fen, move);
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

        public final Builder setVariant(Variant variant) {
            this.variant = variant;
            return this;
        }

        public final Builder setOption(String name, int value) {
            options.add(new Option(name, value));
            return this;
        }

        public final StockfishClient build() {
            return new StockfishClient(variant, options.toArray(new Option[0]));
        }
    }
}
