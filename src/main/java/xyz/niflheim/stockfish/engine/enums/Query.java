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

public class Query {
    private QueryType type;
    private String fen, move;
    private int difficulty, depth;
    private long movetime;

    public Query(QueryType type, String fen, int difficulty, int depth, long movetime) {
        this.type = type;
        this.fen = fen;
        this.difficulty = difficulty;
        this.depth = depth;
        this.movetime = movetime;
    }

    public QueryType getType() {
        return type;
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

    public int getDepth() {
        return depth;
    }

    public long getMovetime() {
        return movetime;
    }

    public static class Builder {
        private QueryType type;
        private String fen, move;
        private int difficulty = -1, depth = -1;
        private long movetime = -1;

        public Builder(QueryType type) {
            this.type = type;
        }

        public Builder setFen(String fen) {
            this.fen = fen;
            return this;
        }

        public Builder setMove(String move) {
            this.move = move;
            return this;
        }

        public Builder setDifficulty(int difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder setDepth(int depth) {
            this.depth = depth;
            return this;
        }

        public Builder setMovetime(long movetime) {
            this.movetime = movetime;
            return this;
        }

        public Query build() {
            if (type == null)
                throw new IllegalStateException("Query type can not be null.");

            if (fen == null)
                throw new IllegalStateException("Query is missing FEN.");

            return new Query(type, fen, difficulty, depth, movetime);
        }
    }
}
