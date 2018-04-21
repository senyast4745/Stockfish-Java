package xyz.stockfish.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.niflheim.utils.Variant;
import xyz.stockfish.ChessEngine;
import xyz.stockfish.ChessEnginePool;
import xyz.stockfish.utils.Option;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class StockfishPool implements ChessEnginePool {
    private static final Logger log = LoggerFactory.getLogger(StockfishPool.class);

    private final BlockingQueue<Consumer<ChessEngine>> queue = new LinkedBlockingQueue<>();
    private final List<Thread> threads = new LinkedList<>();

    public StockfishPool(Variant variant, int workers, Option... options) throws StockfishInitException {
        for (int i = 0; i < workers; i++) {

            int id = i;
            threads.add(new Thread("StockfishWorker-" + id) {
                Stockfish worker = new Stockfish(variant, options);

                {
                    setDaemon(true);
                    start();
                }

                @Override
                public void run() {
                    while (true) {
                        try {
                            if (!worker.isAlive()) {
                                log.error("Worker #" + id + " detected dead Stockfish instance, respawning...");

                                try {
                                    worker.close();
                                } catch (Exception ignored) {
                                }
                                worker = null;

                                try {
                                    worker = new Stockfish(variant, options);
                                } catch (Exception e) {
                                    log.error("Worker #" + id + " couldn't initialize new Stockfish instance.", e);
                                    threads.remove(this);
                                    return;
                                }
                            }
                            queue.take().accept(worker);
                        } catch (InterruptedException e) {
                            try {
                                threads.remove(this);
                                worker.close();
                            } catch (IOException ignored) {
                            }
                            return;
                        } catch (Exception e) {
                            log.error("Worker #" + id + " caught an unhandled exception:", e);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        threads.forEach(Thread::interrupt);
    }

    @Override
    public void submit(Consumer<ChessEngine> consumer) {
        queue.offer(consumer);
    }
}
