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
package xyz.niflheim.stockfish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;
import xyz.niflheim.stockfish.pool.Move;
import xyz.niflheim.stockfish.pool.StockfishPool;
import xyz.niflheim.stockfish.utils.MoveType;
import xyz.niflheim.stockfish.utils.Option;
import xyz.niflheim.stockfish.utils.Variant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StockfishClient {
    private Logger LOG = LoggerFactory.getLogger(StockfishClient.class);
    private StockfishPool pool;

    public StockfishClient(int instances, Variant variant, Set<Option> options) {
        pool = new StockfishPool(instances, variant, Arrays.copyOf(options.toArray(), options.size(), Option[].class));
    }

    public String makeMove(String fen, String pgn) {
        return pool.execute(new Move(MoveType.makeMove).setFen(fen).setPgn(pgn));
    }

    public String getCheckers(String fen) {
        return pool.execute(new Move(MoveType.getCheckers).setFen(fen));
    }

    public String getBestMove(String fen, int difficulty, int depth, int movetime) {
        return pool.execute(new Move(MoveType.bestMove).setFen(fen).setDifficulty(difficulty).setDepth(depth).setMovetime(movetime));
    }

    public String getLegalMoves(String fen) {
        return pool.execute(new Move(MoveType.legalMoves).setFen(fen));
    }

    public static class Builder {
        private Set<Option> options = new HashSet<>();
        private int instances = 1;
        private Variant variant;

        public final Builder setInstances(int num) {
            instances = num;
            return this;
        }

        public final Builder setVariant(Variant v) {
            variant = v;
            return this;
        }

        public final Builder setOption(Option o, int value) {
            options.add(o.setValue(value));
            return this;
        }

        public final StockfishClient build() throws StockfishInitException {
            if (variant == null)
                throw new StockfishInitException("Variant cannot be null!");
            else
                return new StockfishClient(instances, variant, options);
        }
    }
}
