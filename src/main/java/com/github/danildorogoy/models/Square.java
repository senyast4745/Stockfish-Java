package com.github.danildorogoy.models;

public class Square {
    private final boolean isWhite;
    private ChessPiece chessPiece;
    private final String coord;

    public Square(boolean isWhite, String coord) {
        this.isWhite = isWhite;
        this.coord = coord;
        chessPiece = ChessPiece.NONE;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public String getCoord() {
        return coord;
    }
}
