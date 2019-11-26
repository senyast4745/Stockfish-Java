package xyz.niflheim.stockfish.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessManager {
    /**
     * Get Stockfish process number.
     *
     * @return Stockfish process number
     * @throws IOException when can not execute Unix command
     */
    public static long getProcessNumber() throws IOException {
        return getProcessNumber("stockfish_10");
    }


    /**
     * @param process the name of the process to be found.
     * @return process number with name {@code process}
     * @throws IOException when can not execute Unix command
     */
    public static long getProcessNumber(String process) throws IOException {
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
    public static void killStockfishProcess() throws IOException {
        Process p = Runtime.getRuntime().exec("ps -few");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<String> pids = input.lines().filter(l -> l.contains("stockfish_10")).collect(Collectors.toList());
        String pid = pids.get(0).split("\\s+")[1];
        Runtime.getRuntime().exec("kill " + pid);
        input.close();
    }
}
