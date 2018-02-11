package engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockfishWrapper implements ChessEngine {
    private final Logger Log = LoggerFactory.getLogger("Stockfish Wrapper");
    private static final String DEFAULT_EXECUTABLE_PATH = StockfishWrapper.class.getResource(System.getProperty("os.name").toLowerCase().contains("win") ? "stockfish_9.exe" : "stockfish_9").getPath();
    private static final long DEFAULT_MOVE_TIME_MILLIS = 25;

    private Process engine;
    private BufferedReader engineIn;
    private BufferedWriter engineOut;

    private String executablePath = DEFAULT_EXECUTABLE_PATH;
    private long moveTimeMillis = DEFAULT_MOVE_TIME_MILLIS;


    @Override
    public boolean start() {
        try {
            engine = Runtime.getRuntime().exec(executablePath);
            engineIn = new BufferedReader(new InputStreamReader(engine.getInputStream()));
            engineOut = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
        } catch (Exception e) {
            Log.error("Exception caught while starting Stockfish: " + e.getMessage());
            return false;
        }

       return true;
    }

    @Override
    public void stop() {
        try {
            sendUCICommand("quit");
            engineIn.close();
            engineOut.close();
            engine.destroy();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String reset() {
        sendUCICommand("position startpos");
        return displayFen();
    }

    @Override
    public String getBestMove(String fen, int difficulty) {
        return null;
    }

    @Override
    public String getLegalMoves(String fen) {
        return null;
    }

    @Override
    public String getCheckers(String fen) {
        return null;
    }

    @Override
    public String getState(String fen) {
        return null;
    }

    @Override
    public String makeMove(String fen, String pgn) {
        waitForReady();
        sendUCICommand("position fen " + fen + " moves " + pgn);
        return displayFen();
    }

    public String displayFen() {
        waitForReady();
        sendUCICommand("d");
        String fen = "";
        List<String> response = readResponse("Checkers:");
        for (int i = response.size() - 1; i >= 0; i--) {
            String line = response.get(i);
            if (line.startsWith("Fen: ")) {
                fen = line.substring("Fen: ".length());
                break;
            }
        }
        return fen;
    }

    private void sendUCIOption(String name, int value) {
        sendUCICommand("setoption name " + name + " value " + value);
    }

    private void sendUCICommand(String command) {
        try {
            engineOut.write(command + "\n");
            engineOut.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> readResponse(String expected) {
        try {
            List<String> lines = new ArrayList<String>();
            while (true) {
                String line = engineIn.readLine();
                lines.add(line);
                if (line.startsWith(expected))
                    break;
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void waitForReady() {
        sendUCICommand("isready");
        readResponse("readyok");
    }
}