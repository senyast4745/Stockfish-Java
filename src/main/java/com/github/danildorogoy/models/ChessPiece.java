package com.github.danildorogoy.models;

public enum ChessPiece {

    BLACK_PAWN("p", "bPawn.png"),
    WHITE_PAWN("P", "wPawn.png"),
    BLACK_ROOK("r", "bRook.png"),
    WHITE_ROOK("R", "wRook.png"),
    BLACK_KNIGHT("n", "bKnight.png"),
    WHITE_KNIGHT("N", "wKnight.png"),
    BLACK_BISHOP("b", "bBishop.png"),
    WHITE_BISHOP("B", "wBishop.png"),
    BLACK_QUEEN("q", "bQueen.png"),
    WHITE_QUEEN("Q", "wQueen.png"),
    BLACK_KING("k", "bKing.png"),
    WHITE_KING("K", "wKing.png"),
    NONE("none","");

    private final String img;

    private final String title;

    ChessPiece(String title, String img) {
        this.img = img;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public static ChessPiece getChessPiece(String fenStr){
        switch (fenStr) {
            case "p": return BLACK_PAWN;
            case "r": return BLACK_ROOK;
            case "n": return BLACK_KNIGHT;
            case "b": return BLACK_BISHOP;
            case "q": return BLACK_QUEEN;
            case "k": return BLACK_KING;
            case "P": return WHITE_PAWN;
            case "R": return WHITE_ROOK;
            case "N": return WHITE_KNIGHT;
            case "B": return WHITE_BISHOP;
            case "Q": return WHITE_QUEEN;
            case "K": return WHITE_KING;
            default: return NONE;
        }
    }
}


