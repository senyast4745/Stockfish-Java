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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * The StockfishClient for managing Stockfish processes,
 * as well as for interacting with the Stockfish API using {@link Query}.
 * <p>
 *
 * @author Niflheim
 * @see <a href="https://stockfishchess.org/">Stockfish website</a>
 * @see <a href="https://github.com/official-stockfish/Stockfish">Official Stochfish <b>Github</b> repository</a>
 * @since 1.0
 */
public class StockfishClient {

    private static final Log log = LogFactory.getLog(StockfishClient.class);

    private ExecutorService executor, callback;
    private Queue<Stockfish> engines;


    /**
     * Private constructor for {@code StockfishClient} which is used by Builder to create a new instance
     *
     * @param path      path to folder with Stockfish core (default assets/engine)
     * @param instances number of Stockfish core that will be launched to process requests asynchronously
     * @param variant   variant of Stockfish core, see {@link xyz.niflheim.stockfish.engine.enums.Variant} enum
     * @param options   Stockfish launch options, see {@link xyz.niflheim.stockfish.engine.enums.Option} enum
     * @throws StockfishInitException throws if Stockfish process can not be initialized, starter or bind
     */
    private StockfishClient(String path, int instances, Variant variant, Set<Option> options) throws StockfishInitException {
        executor = Executors.newFixedThreadPool(instances);
        callback = Executors.newSingleThreadExecutor();
        engines = new ArrayBlockingQueue<>(instances);

        for (int i = 0; i < instances; i++)
            engines.add(new Stockfish(path, variant, options.toArray(new Option[0])));
    }

    /**
     * Method to execute UCI command as Query in Stockfish without callback.
     *
     * @param query query to execute in Stockfish
     * @see xyz.niflheim.stockfish.engine.enums.Query
     */
    @SuppressWarnings("unused")
    public void submit(Query query) {
        submit(query, null);
    }


    /**
     * Method to execute UCI command as Query in Stockfish with callback.
     *
     * @param query  query to execute in Stockfish
     * @param result callback after executing query in Stockfish
     * @see xyz.niflheim.stockfish.engine.enums.Query
     */
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


    /**
     * This method close all Stockfish instances that were created, as well as close all
     * threads for processing responses. You must call this method when you close
     * your program to avoid uncontrolled memory leaks.
     * <p>
     * Exceptions are thrown only after trying to close all remaining threads
     *
     * @throws StockfishEngineException when at least one of the processes could not be closed.
     */
    public void close() throws StockfishEngineException {

        awaitTerminationAfterShutdown(executor);
        awaitTerminationAfterShutdown(callback);

        AtomicBoolean error = new AtomicBoolean(false);
        AtomicReference<Exception> ex = new AtomicReference<>();
        engines.parallelStream().forEach(engine -> {
            try {
                engine.close();
            } catch (IOException | StockfishEngineException e) {
                ex.set(e);
                error.compareAndSet(false, true);
                log.fatal("Can not stop Stockfish. Please, close it manually.", e);
            }
        });
        if (error.get()) {
            throw new StockfishEngineException("Error while closing Stockfish threads", ex.get());
        }
    }

    private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
        }
    }


    /**
     * Standard Builder pattern to create {@link StockfishClient} instance.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Wiki <b>Builder</b> pattern.</a>
     */
    public static class Builder {
        private Set<Option> options = new HashSet<>();
        private Variant variant = Variant.DEFAULT;
        private String path = null;
        private int instances = 1;

        /**
         * @param num number of Stockfish core that will be launched to process requests asynchronously
         * @return Builder to continue creating StockfishClient
         */
        public final Builder setInstances(int num) {
            instances = num;
            return this;
        }

        /**
         * @param v variant of Stockfish core, see {@link xyz.niflheim.stockfish.engine.enums.Variant} enum
         * @return Builder to continue creating StockfishClient
         */
        public final Builder setVariant(Variant v) {
            variant = v;
            return this;
        }

        /**
         * @param o     Stockfish launch options, see {@link xyz.niflheim.stockfish.engine.enums.Option} enum
         * @param value value of option
         * @return Builder to continue creating StockfishClient
         */
        public final Builder setOption(Option o, long value) {
            options.add(o.setValue(value));
            return this;
        }

        /**
         * @param path path to folder with Stockfish core (default assets/engine/)
         * @return Builder to continue creating StockfishClient
         */
        public final Builder setPath(String path) {
            this.path = path;
            return this;
        }

        /**
         * @return ready StockfishClient with fields set
         * @throws StockfishInitException throws if Stockfish process can not be initialized, starter or bind
         */
        public final StockfishClient build() throws StockfishInitException {
            return new StockfishClient(path, instances, variant, options);
        }
    }
}
