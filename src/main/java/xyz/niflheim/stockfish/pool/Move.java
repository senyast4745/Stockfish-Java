/* Copyright 2018 David Cai Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.niflheim.stockfish.pool;

import xyz.niflheim.stockfish.engine.Stockfish;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.utils.MoveType;

import java.util.concurrent.Callable;

public class Move implements Callable<String> {
    private Stockfish stockfish;
    private MoveType type;

    private String fen, pgn;

    private int difficulty, depth, movetime;

    public Move(MoveType type) {
        this.type = type;
    }

    public Move setStockfish(Stockfish stockfish) {
        this.stockfish = stockfish;
        return this;
    }

    public Move setFen(String fen) {
        this.fen = fen;
        return this;
    }

    public Move setPgn(String pgn) {
        this.pgn = pgn;
        return this;
    }

    public Move setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Move setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public Move setMovetime(int movetime) {
        this.movetime = movetime;
        return this;
    }

    public String call() {
        switch (type) {
            case makeMove:
                return stockfish.makeMove(fen, pgn);
            case bestMove:
                return stockfish.getBestMove(fen, difficulty, depth, movetime);
            case legalMoves:
                return stockfish.getLegalMoves(fen);
            case getCheckers:
                return stockfish.getCheckers(fen);
            default:
                throw new StockfishEngineException("Illegal move.");
        }
    }
}
