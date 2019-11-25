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

/**
 * Types of query for execution in Stockfish.
 *
 * @author Niflheim
 * @since 1.0
 */
public enum QueryType {
    /**
     * Get best move for this FEN position.
     */
    Best_Move,
    /**
     * Get a response move to the user's move.
     */
    Make_Move,
    /**
     * Get a list of all legal moves for this FEN position.
     */
    Legal_Moves,
    /**
     *
     */
    Checkers
}
