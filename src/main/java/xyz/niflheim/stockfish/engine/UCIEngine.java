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
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

abstract class UCIEngine {
    final BufferedReader input;
    final BufferedWriter output;
    final Process process;

    UCIEngine(String path, Variant variant, Option... options) throws StockfishInitException {
        try {
            process = Runtime.getRuntime().exec(getPath(variant, path));
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            for (Option option : options)
                passOption(option);
        } catch (IOException e) {
            throw new StockfishInitException("Unable to start and bind Stockfish process: ", e);
        }
    }

    void waitForReady() {
        sendCommand("isready");
        readResponse("readyok");
    }

    void sendCommand(String command) {
        try {
            output.write(command + "\n");
            output.flush();
        } catch (IOException e) {
            throw new StockfishEngineException(e);
        }
    }

    String readLine(String expected) {
        try {
            return input.lines().sequential().filter(l -> l.startsWith(expected)).findFirst()
                    .orElseThrow(() -> new StockfishEngineException("Can not find expected line: " + expected));
        } catch (UncheckedIOException e) {
            throw new StockfishEngineException(e);
        }
    }

    List<String> readResponse(String expected) {
        try {
            List<String> lines = new ArrayList<>();
            String line;
            boolean isPresent = false;
            while ((line = input.readLine()) != null) {
                lines.add(line);

                if (line.startsWith(expected)) {
                    isPresent = true;
                    break;
                }
            }
            if (isPresent) {
                return lines;
            } else {
                throw new StockfishEngineException("Can not find expected line: " + expected);
            }
        } catch (IOException e) {
            throw new StockfishEngineException(e);
        }
    }

    private void passOption(Option option) {
        sendCommand(option.toString());
    }

    private String getPath(Variant variant, String override) {
        StringBuilder path = new StringBuilder(override == null ? "assets/engines/stockfish_10_x64" : override + "stockfish_10_x64");

        if (System.getProperty("os.name").toLowerCase().contains("win"))
            switch (variant) {
                case DEFAULT:
                    path.append(".exe");
                    break;
                case BMI2:
                    path.append("_bmi2.exe");
                    break;
                case POPCNT:
                    path.append("_popcnt.exe");
                    break;
                default:
                    throw new StockfishEngineException("Illegal variant provided.");
            }
        else
            switch (variant) {
                case DEFAULT:
                    break;
                case BMI2:
                    path.append("_bmi2");
                    break;
                case MODERN:
                    path.append("_modern");
                    break;
                default:
                    throw new StockfishEngineException("Illegal variant provided.");
            }

        return path.toString();
    }
}
