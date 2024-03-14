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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xyz.niflheim.stockfish.engine.util.Util.*;

abstract class UCIEngine {

    private static final Log log = LogFactory.getLog(UCIEngine.class);
    final BufferedReader input;
    final BufferedWriter output;
    final Process process;
    int engineVersion = 0;

    UCIEngine(String path, Variant variant, Integer engineVersion, Option... options) throws StockfishInitException {
        try {

            process = Runtime.getRuntime().exec(getPath(variant, path, engineVersion));
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            for (Option option : options)
                passOption(option);
        } catch (IOException e) {
            throw new StockfishInitException("Unable to start and bind Stockfish process: ", e);
        }
    }

    UCIEngine(String path, Variant variant, Option... options) throws StockfishInitException {
        this(path, variant, null, options);
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
        return getPath(variant, override, null);
    }

    private String getPath(Variant variant, String override, Integer engineVersion) {
        Optional<Integer> versionOptional = SUPPORTED_VERSIONS.values().stream().filter(v -> v == engineVersion).findFirst();
        if (versionOptional.isPresent()) {
            this.engineVersion = versionOptional.get();
        } else {
            if (engineVersion != null &&  engineVersion != SUPPORTED_VERSIONS.entrySet().iterator().next().getValue()) {
                log.info("Version " + engineVersion + " not found. Defaulting to " + SUPPORTED_VERSIONS.entrySet().iterator().next().getValue());
            }
            this.engineVersion = SUPPORTED_VERSIONS.values().stream().max(Integer::compareTo).get();
        }
        StringBuilder path = new StringBuilder(override == null ?
                ASSETS_LOCATION + ENGINE_FILE_NAME_PREFIX + this.engineVersion + ENGINE_FILE_NAME_SUFFIX :
                override +  ENGINE_FILE_NAME_PREFIX + this.engineVersion + ENGINE_FILE_NAME_SUFFIX);

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
