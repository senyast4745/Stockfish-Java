package xyz.niflheim;

import xyz.niflheim.utils.Variant;

public class ClientTester {
    public static void main(String[] args) {
        StockfishClient client = new StockfishClient.Builder()
                .setOption("Threads", Runtime.getRuntime().availableProcessors())
                .setVariant(Variant.DEFAULT)
                .build();

        System.out.println("Starting FEN: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println("Best move: " + client.getBestMove("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 20));
        System.out.println("Making move e2e4...");

        System.out.println("New FEN: " + client.makeMove(client.getFen(), "e2e4"));
    }
}
