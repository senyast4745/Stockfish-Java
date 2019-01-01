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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.utils.Option;
import xyz.niflheim.stockfish.utils.Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class StockfishPool {
    private Logger LOG = LoggerFactory.getLogger(StockfishPool.class);
    private List<StockfishWorker> workers;
    private int index;

    public StockfishPool(int instances, Variant variant, Option... options) {
        workers = new ArrayList<>();
        index = 0;

        for (int i = 0; i < instances; i++) {
            StockfishWorker worker = new StockfishWorker(i, variant, options);
            workers.add(worker);
        }
    }

    public String execute(Move move) {
        System.out.println("Getting result from worker " + index);
        Future<String> result = workers.get(index).submit(move);

        if (++index == workers.size())
            index = 0;

        try {
            return result.get();
        } catch (Exception e) {
            throw new StockfishEngineException(e);
        }
    }
}
