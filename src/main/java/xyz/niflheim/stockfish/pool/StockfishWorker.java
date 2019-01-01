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
package xyz.niflheim.stockfish.pool;

import xyz.niflheim.stockfish.engine.Stockfish;
import xyz.niflheim.stockfish.utils.Option;
import xyz.niflheim.stockfish.utils.Variant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StockfishWorker {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Stockfish stockfish;
    private int i;

    public StockfishWorker(int num, Variant variant, Option... options) {
        stockfish = new Stockfish(variant, options);
        i=num;
    }

    public Future<String> submit(Move move) {
        System.out.println("Working " + i);
        return executor.submit(move.setStockfish(stockfish));
    }
}
