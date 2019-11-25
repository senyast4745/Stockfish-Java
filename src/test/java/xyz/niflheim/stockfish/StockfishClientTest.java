package xyz.niflheim.stockfish;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.util.OSValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test to open/close Stockfish Client.
 *
 * @author senyast4745
 * @since 2.0.2_2
 */
class StockfishClientTest {

    private final static Log logger = LogFactory.getLog(StockfishClientTest.class);

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
                        .setOption(Option.Threads, 4) // Number of threads that Stockfish will use
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


            } catch (Throwable t) {
                logger.error("Fail ", t);
                fail(t);
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
            } catch (Throwable t) {
                logger.error("Fail ", t);
                fail(t);
            }
        }
    }


    /**
     * Get Stockfish process number.
     *
     * @return Stockfish process number
     * @throws IOException when can not execute Unix command
     */
    private long getProcessNumber() throws IOException {
        return getProcessNumber("stockfish_10");
    }


    /**
     * @param process the name of the process to be found.
     * @return process number with name {@code process}
     * @throws IOException when can not execute Unix command
     */
    private long getProcessNumber(String process) throws IOException {
        Process p = Runtime.getRuntime().exec("ps -few");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        long ans = input.lines().filter(l -> l.contains(process)).count();
        input.close();
        return ans;
    }

    /**
     * Kill one Stockfish process.
     *
     * @throws IOException when can not execute Unix command
     */
    private void killStockfishProcess() throws IOException {
        Process p = Runtime.getRuntime().exec("ps -few");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<String> pids = input.lines().filter(l -> l.contains("stockfish_10")).collect(Collectors.toList());
        String pid = pids.get(0).split("\\s+")[1];
        Runtime.getRuntime().exec("kill " + pid);
        input.close();
    }
}