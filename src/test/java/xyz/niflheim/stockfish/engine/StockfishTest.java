package xyz.niflheim.stockfish.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.niflheim.stockfish.StockfishClient;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StockfishTest {

    public static final Random random = new Random();
    private static final Log log = LogFactory.getLog(StockfishTest.class);
    private StockfishClient stockfishClient;

    @BeforeEach
    void setUp() {
        try {
            stockfishClient = StockfishClient.createDefault();
        } catch (StockfishInitException e) {
            log.error("error while create Stockfish client: ", e);
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            stockfishClient.close();
        } catch (StockfishEngineException e) {
            log.error("error while close Stockfish client: ", e);
            fail(e);
        }
    }

    @Test
    void waitForReady() {
    }

    @Test
    void sendCommand() {
        try {
            File tempFile = creteTempFile();
            Stockfish stockfish = new Stockfish(null, Variant.DEFAULT);
            Field output = stockfish.getClass().getSuperclass().getDeclaredField("output");
            output.setAccessible(true);
            output.set(stockfish, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile))));
            output.setAccessible(false);


            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));

            stockfish.sendCommand("hello world");

            assertEquals("hello world", reader.readLine());
        } catch (IOException | NoSuchFieldException | StockfishInitException | IllegalAccessException e) {
            log.error("test error: ", e);
            fail(e);
        }
    }

    @Test
    void readLine() {
        try {

            Buf

            File tempFile = creteTempFile();
            Stockfish stockfish = new Stockfish(null, Variant.DEFAULT);
            Field input = stockfish.getClass().getSuperclass().getDeclaredField("input");
            input.setAccessible(true);
            input.set(stockfish, new BufferedReader(new InputStreamReader(new FileInputStream(tempFile))));
            input.setAccessible(false);
        } catch (IOException | StockfishInitException | NoSuchFieldException | IllegalAccessException e) {
            log.error("test error: ", e);
            fail(e);
        }
    }

    @Test
    void readResponse() {
    }

    @Test
    void makeMove() {
    }

    @Test
    void getCheckers() {
    }

    @Test
    void getBestMove() {
    }

    @Test
    void getLegalMoves() {
    }

    @Test
    void close() {
    }

    private File creteTempFile() throws IOException {
        return File.createTempFile("stockfish-", ".tmp");
    }
}