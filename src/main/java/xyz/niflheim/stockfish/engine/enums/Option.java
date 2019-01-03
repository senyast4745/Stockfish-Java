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

public enum Option {
    Contempt("Contempt"),
    Analysis_Contempt("Analysis Contempt"),
    Threads("Threads"),
    Hash("Hash"),
    Clear_Hash("Clear Hash"),
    Ponder("Ponder"),
    MultiPV("MultiPV"),
    Skill_Level("Skill Level"),
    Move_Overhead("Move Overhead"),
    Minimum_Thinking_Time("Minimum Thinking Time"),
    Slow_Mover("Slow Mover"),
    Nodestime("nodestime");

    private String optionString;
    private long value;

    Option(String option) {
        optionString = option;
    }

    public Option setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "setoption name " + optionString + " value " + value;
    }
}
