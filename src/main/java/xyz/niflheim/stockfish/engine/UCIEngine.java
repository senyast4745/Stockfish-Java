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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.utils.Option;
import xyz.niflheim.stockfish.utils.Variant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class UCIEngine {
    public final Logger LOG = LoggerFactory.getLogger(UCIEngine.class);
    public final BufferedReader input;
    public final BufferedWriter output;
    public final Process process;

    public UCIEngine(Variant variant, Option... options) {
        try {
            process = Runtime.getRuntime().exec(getPath(variant));
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            for (Option option : options)
                passOption(option);
        } catch (Exception e) {
            throw new StockfishEngineException(e);
        }
    }

    public void waitForReady() {
        sendCommand("isready");
        readResponse("readyok");
    }

    public void sendCommand(String command) {
        try {
            output.write(command + "\n");
            output.flush();
        } catch (IOException e) {
            throw new StockfishEngineException(e);
        }
    }

    public List<String> readResponse(String expected) {
        try {
            List<String> lines = new ArrayList<>();
            while (true) {
                String line = input.readLine();
                lines.add(line);

                if (line.startsWith(expected))
                    break;
            }
            return lines;
        } catch (IOException e) {
            throw new StockfishEngineException(e);
        }
    }

    public void passOption(Option option) {
        sendCommand(option.toString());
    }

    private String getPath(Variant variant) {
        StringBuilder path = new StringBuilder("assets/engines/stockfish_10_x64");

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
