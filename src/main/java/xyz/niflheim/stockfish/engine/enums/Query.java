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
package xyz.niflheim.stockfish.engine.enums;

import java.util.regex.Pattern;

import static xyz.niflheim.stockfish.engine.enums.QueryType.Best_Move;

/**
 * Class Query to Stockfish, which will be converted to UCI
 */
public class Query {

    public static final int MAX_DEPTH = 15;

    private static final int MAX_TIME = 4000; //4 sec
    private QueryType type;
    private String fen, move;
    private int difficulty, depth;
    private long movetime;

    @SuppressWarnings("WeakerAccess")
    public Query(QueryType type, String fen, int difficulty, int depth, long movetime) {
        this.type = type;
        this.fen = fen;
        this.difficulty = difficulty;
        this.depth = depth;
        this.movetime = movetime;
    }

    @SuppressWarnings("WeakerAccess")
    public Query(QueryType type, String fen, String move, int difficulty, int depth, long movetime) {
        this.type = type;
        this.fen = fen;
        this.move = move;
        this.difficulty = difficulty;
        this.depth = depth;
        this.movetime = movetime;
    }

    /**
     * @return type of UCI query
     * @see QueryType
     * @see <a href="http://wbec-ridderkerk.nl/html/UCIProtocol.html">Univesal Chess Protocol Documentation</a>
     */
    public QueryType getType() {
        return type;
    }

    /**
     * @return FEN chessboard position as string
     * @see <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Wiki FEN</a>
     */
    public String getFen() {
        return fen;
    }

    /**
     * @return users move as string in USI
     * @see <a href="http://wbec-ridderkerk.nl/html/UCIProtocol.html">Univesal Chess Protocol Documentation</a>
     */
    public String getMove() {
        return move;
    }

    /**
     * @return the difficulty that will be installed on Stockfish when considering a move,
     * if the number is less than 1, then the standard difficulty will be set
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @return the depth to which Stockfish will calculate the moves.
     * if the number is less than 1, then the standard difficulty will be set
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return the minimum time that Stockfish will think about its moves.
     * if the number is less than 1, then the standard difficulty will be set
     */
    public long getMovetime() {
        return movetime;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setMovetime(int movetime) {
        this.movetime = movetime;
    }


    private boolean isWithinLimits() {
        if (Best_Move.equals(getType()) && ((getDepth() < 0 || getMovetime() < 0)
                || (getDepth() > MAX_DEPTH || getMovetime() > MAX_TIME))) {
            return false;
        }
        return true;
    }

    public void normalize() {
        if (!isWithinLimits()) {
            if(getDepth() < 0 || getDepth() > MAX_DEPTH) {
                setDepth(MAX_DEPTH);
            }
            if(getMovetime() < 0 || getMovetime() > MAX_TIME) {
                setMovetime(MAX_TIME);
            }
        }
    }

    /**
     * Standard Builder pattern to create {@link Query} instance.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Wiki <b>Builder</b> pattern.</a>
     */
    public static class Builder {
        private static final String START_REGEX = "^";
        private static final String END_REGEX = "$";
        private static final String FEN_REGEX = "(([rnbqkp1-8PRNBQK]{1,8}/){7}[rnbqkp1-8PRNBQK]{1,8})" +
                "(\\s)([wb])(\\s[-kqKQ]{1,4}\\s)((-)|[a-h][1-8])(\\s)([0-9]+)(\\s)([0-9]+)";
        private static final String MOVE_REGEX = "([a-h][1-8]){2}";

        private static final Pattern fenPattern = Pattern.compile(START_REGEX + FEN_REGEX + END_REGEX);
        private static final Pattern movePattern = Pattern.compile(START_REGEX + MOVE_REGEX + END_REGEX);
        private QueryType type;
        private String fen, move;
        private int difficulty = -1, depth = -1;
        private long movetime = -1;

        /**
         * @param type type of UCI query
         * @param fen  FEN chessboard position as string
         * @see QueryType
         * @see <a href="http://wbec-ridderkerk.nl/html/UCIProtocol.html">Univesal Chess Protocol Documentation</a>
         * @see <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Wiki FEN</a>
         */
        public Builder(QueryType type, String fen) {
            this.fen = fen;
            this.type = type;
        }

        /**
         * @param move users move in USI
         * @return Builder
         * @throws IllegalArgumentException if the incoming line is not a chess move
         */
        public Builder setMove(String move) throws IllegalArgumentException {
            if (move == null || !movePattern.matcher(move).matches()) {
                throw new IllegalArgumentException("Incorrect Move in Query: " + move);
            }
            this.move = move;
            return this;
        }

        /**
         * @param difficulty the difficulty that will be installed on Stockfish when considering a move,
         *                   if the number is less than 1, then the standard difficulty will be set
         * @return Builder
         */
        public Builder setDifficulty(int difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        /**
         * @param depth the depth to which Stockfish will calculate the moves.
         *              if the number is less than 1, then the standard difficulty will be set
         * @return Builder
         */
        public Builder setDepth(int depth) {
            this.depth = depth;
            return this;
        }

        /**
         * @param movetime the minimum time that Stockfish will think about its moves.
         *                 if the number is less than 1, then the standard difficulty will be set
         * @return Builder
         */
        public Builder setMovetime(long movetime) {
            this.movetime = movetime;
            return this;
        }

        /**
         * Build Query.
         *
         * @return query to be converted to a UCI request to StockFish
         * @throws IllegalArgumentException if the incoming line is not in FEN
         * @throws IllegalStateException if QueryType or FEN is null
         */
        public Query build() throws IllegalArgumentException, IllegalStateException {
            if (type == null)
                throw new IllegalStateException("Query type can not be null.");

            if (fen == null)
                throw new IllegalStateException("Query is missing FEN.");

            if (!fenPattern.matcher(fen).matches()) {
                throw new IllegalArgumentException("Incorrect FEN in Query: " + fen);
            }
            if (move != null) {
                return new Query(type, fen, move, difficulty, depth, movetime);
            }

            return new Query(type, fen, difficulty, depth, movetime);
        }
    }
}
