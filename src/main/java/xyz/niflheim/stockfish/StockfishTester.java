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
package xyz.niflheim.stockfish;

import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.QueryType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StockfishTester {

    final static Log logger = LogFactory.getLog(StockfishTester.class);

    public static void main(String[] args) throws StockfishInitException {
        int instanceNumber = 4;
        StockfishClient client = new StockfishClient.Builder()
                .setInstances(instanceNumber)
                .setPath("/home/arseny/WorkingFolder/IdeaProjects/Stockfish-Java/assets/engines/")
                .setOption(Option.Threads, 4) // Number of threads that Stockfish will use
                .setVariant(Variant.BMI2)
                .build();
        client.close();
    }
}
