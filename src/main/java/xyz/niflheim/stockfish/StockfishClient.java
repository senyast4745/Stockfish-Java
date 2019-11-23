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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.Stockfish;
import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class StockfishClient {

    private final Log logger = LogFactory.getLog(StockfishClient.class);

    private ExecutorService executor, callback;
    private Queue<Stockfish> engines;

    private StockfishClient(String path, int instances, Variant variant, Set<Option> options) throws StockfishInitException {
        executor = Executors.newFixedThreadPool(instances);
        callback = Executors.newSingleThreadExecutor();
        engines = new ArrayBlockingQueue<>(instances);

        for (int i = 0; i < instances; i++)
            engines.add(new Stockfish(path, variant, options.toArray(new Option[0])));
    }

    public void submit(Query query) {
        submit(query, null);
    }

    public void submit(Query query, Consumer<String> result) {
        executor.submit(() -> {
            Stockfish engine = engines.remove();
            String output;

            switch (query.getType()) {
                case Best_Move:
                    output = engine.getBestMove(query);
                    break;
                case Make_Move:
                    output = engine.makeMove(query);
                    break;
                case Legal_Moves:
                    output = engine.getLegalMoves(query);
                    break;
                case Checkers:
                    output = engine.getCheckers(query);
                    break;
                default:
                    output = null;
                    break;
            }

            callback.submit(() -> result.accept(output));
            engines.add(engine);
        });
    }

    public void close() {
        AtomicBoolean error = new AtomicBoolean(false);
        AtomicReference<Exception> ex = new AtomicReference<>();
        engines.parallelStream().forEach(engine -> {
            try {
                engine.close();
            } catch (IOException | StockfishEngineException e) {
                ex.set(e);
                error.compareAndSet(false, true);
                logger.fatal("Can not stop Stockfish. Please, close it manually.", e);
            }
        });
        executor.shutdown();
        callback.shutdown();
        if (error.get()) {
            throw new StockfishEngineException("Error while closing Stockfish threads", ex.get());
        }
    }

    public static class Builder {
        private Set<Option> options = new HashSet<>();
        private Variant variant = Variant.DEFAULT;
        private String path = null;
        private int instances = 1;

        public final Builder setInstances(int num) {
            instances = num;
            return this;
        }

        public final Builder setVariant(Variant v) {
            variant = v;
            return this;
        }

        public final Builder setOption(Option o, long value) {
            options.add(o.setValue(value));
            return this;
        }

        public final Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public final StockfishClient build() throws StockfishInitException {
            return new StockfishClient(path, instances, variant, options);
        }
    }
}
