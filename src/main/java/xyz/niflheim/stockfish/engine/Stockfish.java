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

import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.IOException;
import java.util.List;

public class Stockfish extends UCIEngine {
    public Stockfish(String path, Variant variant, Option... options) throws StockfishInitException {
        super(path, variant, options);
    }

    public String makeMove(Query query) {
        waitForReady();
        sendCommand("position fen " + query.getFen() + " moves " + query.getMove());
        return getFen();
    }

    public String getCheckers(Query query) {
        waitForReady();
        sendCommand("position fen " + query.getFen());

        waitForReady();
        sendCommand("d");

        return readLine("Checkers: ").substring(10);
    }

    public String getBestMove(Query query) {
        if (query.getDifficulty() >= 0) {
            waitForReady();
            sendCommand("setoption name Skill Level value " + query.getDifficulty());
        }

        waitForReady();
        sendCommand("position fen " + query.getFen());

        StringBuilder command = new StringBuilder("go ");

        if (query.getDepth() >= 0)
            command.append("depth ").append(query.getDepth()).append(" ");

        if (query.getMovetime() >= 0)
            command.append("movetime ").append(query.getMovetime());

        waitForReady();
        sendCommand(command.toString());

        return readLine("bestmove").substring(9).split("\\s+")[0];
    }

    public String getLegalMoves(Query query) {
        waitForReady();
        sendCommand("position fen " + query.getFen());

        waitForReady();
        sendCommand("go perft 1");

        StringBuilder legal = new StringBuilder();
        List<String> response = readResponse("Nodes");

        for (String line : response)
            if (!line.isEmpty() && !line.contains("Nodes") && line.contains(":"))
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

    private String getFen() {
        waitForReady();
        sendCommand("d");

        return readLine("Fen: ").substring(5);
    }
}
