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
 * Stockfish Options. The description of the options was taken from the official Github repository.
 *
 * @author Niflheim
 * @see <a href="https://github.com/official-stockfish/Stockfish">Official Stochfish <b>Github</b> repository</a>
 * @since 1.0
 */

@SuppressWarnings("unused")
public enum Option {
    /**
     * A positive value for contempt favors middle game positions and avoids draws.
     */
    Contempt("Contempt"),
    /**
     * The number of CPU threads used for searching a position.
     * For best performance, set this equal to the number of CPU cores available.
     */
    Threads("Threads"),
    /**
     * The size of the hash table in MB.
     */
    Hash("Hash"),
    /**
     * Clear the hash table.
     */
    Clear_Hash("Clear Hash"),
    /**
     * Let Stockfish ponder its next move while the opponent is thinking.
     */
    Ponder("Ponder"),
    /**
     * Output the N best lines (principal variations, PVs) when searching. Leave at 1 for best performance.
     */
    MultiPV("MultiPV"),
    /**
     * Lower the Skill Level in order to make Stockfish play weaker (see also UCI_LimitStrength).
     * Internally, MultiPV is enabled, and with a certain probability depending on the Skill Level a
     * weaker move will be played.
     */
    Skill_Level("Skill Level"),
    /**
     * Assume a time delay of x ms due to network and GUI overheads.
     * This is useful to avoid losses on time in those cases.
     */
    Move_Overhead("Move Overhead"),
    /**
     * Search for at least x ms per move.
     */
    Minimum_Thinking_Time("Minimum Thinking Time"),
    /**
     * Lower values will make Stockfish take less time in games, higher values will make it think longer.
     */
    Slow_Mover("Slow Mover"),
    /**
     * Tells the engine to use nodes searched instead of wall time to account for elapsed time.
     * Useful for engine testing.
     */
    Nodestime("nodestime");

    private String optionString;
    private long value;

    Option(String option) {
        optionString = option;
    }

    /**
     * Setter for value to Stockfish option.
     *
     * @param value option value
     * @return option for Stockfish
     */
    public Option setValue(long value) {
        this.value = value;
        return this;
    }

    /**
     * Generate UCI command string to set option to Stockfish.
     *
     * @return UCI command to Stockfish
     */
    @Override
    public String toString() {
        return "setoption name " + optionString + " value " + value;
    }
}
