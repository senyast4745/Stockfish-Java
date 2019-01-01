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
package xyz.niflheim.stockfish.engine;

import xyz.niflheim.stockfish.utils.Option;
import xyz.niflheim.stockfish.utils.Variant;

import java.io.IOException;
import java.util.List;

public class Stockfish extends UCIEngine {
    public Stockfish(Variant variant, Option... options) {
        super(variant, options);
    }

    public String setFen(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);
        return fen;
    }

    public String getFen() {
        waitForReady();
        sendCommand("d");

        String fen = "";
        List<String> response = readResponse("Checkers:");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("Fen: ")) {
                fen = line.substring("Fen: ".length());
                break;
            }
        }
        return fen;
    }

    public String makeMove(String pgn) {
        return makeMove(getFen(), pgn);
    }

    public String makeMove(String fen, String pgn) {
        waitForReady();
        sendCommand("position fen " + fen + " moves " + pgn);
        return getFen();
    }

    public String getCheckers() {
        return getCheckers(getFen());
    }

    public String getCheckers(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("d");

        StringBuilder checkers = new StringBuilder();
        List<String> response = readResponse("Checkers:");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("Checkers: ")) {
                for (String pos : line.substring("Checkers: ".length()).split("\\s+"))
                    checkers.append(pos).append(" ");
                break;
            }
        }

        return checkers.toString();
    }

    public String getBestMove(int difficulty, int depth, int movetime) {
        return getBestMove(getFen(), difficulty, depth, movetime);
    }

    public String getBestMove(String fen, int difficulty, int depth, int movetime) {
        waitForReady();
        sendCommand("setoption name Skill Level value " + difficulty);

        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("go depth " + depth + " movetime " + movetime);

        String bestmove = "";
        List<String> response = readResponse("bestmove");

        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("bestmove")) {
                bestmove = line.substring("bestmove ".length());
                break;
            }
        }

        return bestmove.split("\\s+")[0];
    }

    public String getLegalMoves() {
        return getLegalMoves(getFen());
    }

    public String getLegalMoves(String fen) {
        waitForReady();
        sendCommand("position fen " + fen);

        waitForReady();
        sendCommand("go perft 1");

        StringBuilder legal = new StringBuilder();
        List<String> response = readResponse("Nodes");

        for (String line : response)
            if (!line.isEmpty() && line.contains(":"))
                legal.append(line.split(":")[0]).append(" ");

        return legal.toString();
    }

    public void close() throws IOException {
        try {
            sendCommand("quit");
        } finally {
            process.destroy();
            input.close();
            output.close();
        }
    }
}
