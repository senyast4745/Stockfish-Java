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

public class StockfishTester {

    final static Log logger = LogFactory.getLog(StockfishTester.class);

    public static void main(String[] args) throws StockfishInitException {
        logger.info("Start");
        StockfishClient client = new StockfishClient.Builder()
                .setInstances(4).setPath("/home/arseny/WorkingFolder/IdeaProjects/Stockfish-Java/assets/engines/")
                .setOption(Option.Threads, 4) // Number of threads that Stockfish will use
                .setOption(Option.Minimum_Thinking_Time, 1000) // Minimum thinking time Stockfish will take
                .setOption(Option.Skill_Level, 10) // Stockfish skill level 0-20
                .setVariant(Variant.BMI2) // Stockfish Variant
                .build();
        client.submit(new Query.Builder(QueryType.Best_Move)
                        .setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                        // .setDifficulty(20) Setting this overrides Skill Level option
                        // .setDepth(62) Setting this makes Stockfish search deeper
                        // .setMovetime(1000) Setting this overrides the minimum thinking time
                        .build(),
                logger::info); // This is handling the result of the query
        client.close();
    }
}
