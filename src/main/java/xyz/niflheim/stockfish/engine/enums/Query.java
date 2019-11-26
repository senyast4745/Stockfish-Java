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

/**
 *
 */
public class Query {
    private QueryType type;
    private String fen, move;
    private int difficulty, depth;
    private long movetime;

    private Query(QueryType type, String fen, int difficulty, int depth, long movetime) {
        this.type = type;
        this.fen = fen;
        this.difficulty = difficulty;
        this.depth = depth;
        this.movetime = movetime;
    }

    private Query(QueryType type, String fen, String move, int difficulty, int depth, long movetime) {
        this.type = type;
        this.fen = fen;
        this.move = move;
        this.difficulty = difficulty;
        this.depth = depth;
        this.movetime = movetime;
    }

    /**
     * @return
     */
    public QueryType getType() {
        return type;
    }

    /**
     * @return
     */
    public String getFen() {
        return fen;
    }

    /**
     * @return
     */
    public String getMove() {
        return move;
    }

    /**
     * @return
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return
     */
    public long getMovetime() {
        return movetime;
    }

    /**
     *
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
         * @param type
         * @param fen
         */
        public Builder(QueryType type, String fen) {
            this.fen = fen;
            this.type = type;
        }

        /**
         * @param move
         * @return
         * @throws IllegalArgumentException
         */
        public Builder setMove(String move) throws IllegalArgumentException {
            if (move == null || !movePattern.matcher(move).matches()) {
                throw new IllegalArgumentException("Incorrect Move in Query: " + move);
            }
            this.move = move;
            return this;
        }

        /**
         * @param difficulty
         * @return
         */
        public Builder setDifficulty(int difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        /**
         * @param depth
         * @return
         */
        public Builder setDepth(int depth) {
            this.depth = depth;
            return this;
        }

        /**
         * @param movetime
         * @return
         */
        public Builder setMovetime(long movetime) {
            this.movetime = movetime;
            return this;
        }

        /**
         * @return
         * @throws IllegalArgumentException
         * @throws IllegalStateException
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
