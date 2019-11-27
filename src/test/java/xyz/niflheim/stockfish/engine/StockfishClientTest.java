package xyz.niflheim.stockfish.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.QueryType;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.util.OSValidator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.niflheim.stockfish.util.ProcessManager.getProcessNumber;
import static xyz.niflheim.stockfish.util.ProcessManager.killStockfishProcess;
import static xyz.niflheim.stockfish.util.StringUtil.*;

/**
 * Integration test to open/close Stockfish Client.
 *
 * @author senyast4745
 * @since 2.0.2_2
 */
class StockfishClientTest {

    private static final Log log = LogFactory.getLog(StockfishClientTest.class);

    /**
     * Standard open/close tests.
     */
    @Test
    void simpleTests() {
        if (OSValidator.isUnix()) {
            try {
                int instanceNumber = 4;
                StockfishClient client = new StockfishClient.Builder()
                        .setInstances(instanceNumber)
                        .setOption(Option.Threads, 4)
                        .setVariant(Variant.BMI2)
                        .build();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertEquals(instanceNumber, getProcessNumber("stockfish_10"));
                client.close();
                assertEquals(0, getProcessNumber("stockfish_10_x64_bmi2"));

                instanceNumber = 2;
                client = new StockfishClient.Builder()
                        .setInstances(instanceNumber)
                        .setPath("assets/engines/")
                        .setOption(Option.Threads, 2)
                        .setVariant(Variant.DEFAULT)
                        .build();
                assertEquals(instanceNumber, getProcessNumber());
                client.close();
                assertEquals(0, getProcessNumber());

                client = new StockfishClient.Builder()
                        .setInstances(instanceNumber)
                        .setOption(Option.Threads, 2)
                        .setVariant(Variant.DEFAULT)
                        .build();
                assertEquals(instanceNumber, getProcessNumber());
                client.close();
                assertEquals(0, getProcessNumber());


            } catch (Exception e) {
                fail(e);
            }
        }
    }

    /**
     * Kill one of Stockfish process and close.
     */
    @Test
    void killOneStockfishTest() {
        if (OSValidator.isUnix()) {
            try {
                int instanceNumber = 4;
                StockfishClient client = new StockfishClient.Builder()
                        .setInstances(instanceNumber)
                        .setPath("assets/engines/")
                        .setOption(Option.Threads, 2)
                        .setVariant(Variant.DEFAULT)
                        .build();
                assertEquals(instanceNumber, getProcessNumber());
                killStockfishProcess();
                assertEquals(instanceNumber - 1, getProcessNumber());
                assertThrows(StockfishEngineException.class, client::close);
                assertEquals(0, getProcessNumber());
            } catch (Exception e) {
                fail(e);
            }
        }
    }

    @Test
    void submit() {
        StockfishClient client = null;
        try {
            client = new StockfishClient.Builder().build();
            Query query = new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a2a4").build();
            BlockingQueue<Throwable> exceptions = new ArrayBlockingQueue<>(4);
            Consumer<String> move = l -> {
                log.info("start executor " + l);
                try {
                    Pattern fenPattern = Pattern.compile(START_REGEX + FEN_REGEX + END_REGEX);
                    assertTrue(fenPattern.matcher(l).matches());
                    exceptions.put(new TestException());
                    log.info("done executor");
                } catch (Throwable t) {
                    try {
                        exceptions.put(t);
                    } catch (InterruptedException ex) {
                        log.info("Interrupted");
                    }
                }
            };
            client.submit(query, move);
            log.info("done main");
            Throwable throwable;
            if (!((throwable = exceptions.take()) instanceof TestException)) {
                throw throwable;
            }

            query = new Query.Builder(QueryType.Best_Move, START_FEN).build();
            move = l -> {
                log.info("start executor " + l);
                try {
                    Pattern movePattern = Pattern.compile(START_REGEX + MOVE_REGEX + END_REGEX);
                    assertTrue(movePattern.matcher(l).matches());
                    exceptions.put(new TestException());
                    log.info("done executor");
                } catch (Throwable t) {
                    try {
                        exceptions.put(t);
                    } catch (InterruptedException ex) {
                        log.info("Interrupted");
                    }
                }
            };
            client.submit(query, move);
            log.info("done main");
            if (!((throwable = exceptions.take()) instanceof TestException)) {
                throw throwable;
            }

            query = new Query.Builder(QueryType.Legal_Moves, START_FEN).build();
            move = l -> {
                log.info("start executor " + l);
                try {
                    Pattern movePattern = Pattern.compile("^([a-h][1-8](\\s)?)+$");
                    assertTrue(movePattern.matcher(l).matches());
                    exceptions.put(new TestException());
                    log.info("done executor");
                } catch (Throwable t) {
                    try {
                        exceptions.put(t);
                    } catch (InterruptedException ex) {
                        log.info("Interrupted");
                    }
                }
            };
            client.submit(query, move);
            log.info("done main");
            if (!((throwable = exceptions.take()) instanceof TestException)) {
                throw throwable;
            }


        } catch (Throwable e) {
            fail(e);
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    private static final class TestException extends Exception {
    }

}