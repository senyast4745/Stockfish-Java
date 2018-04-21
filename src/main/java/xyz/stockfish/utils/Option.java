package xyz.stockfish.utils;

public class Option {
    private final String name;
    private final int val;

    public Option(String name, int val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public int getVal() {
        return val;
    }
}
