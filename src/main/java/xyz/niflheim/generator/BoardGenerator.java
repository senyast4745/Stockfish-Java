package xyz.niflheim.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BoardGenerator {
    private static final Logger log = LoggerFactory.getLogger(BoardGenerator.class);

    private final BufferedImage gameBoard;
    private final Map<Character, BufferedImage> pieceMap = new HashMap<>();

    public BoardGenerator() throws IOException {

        try {
            gameBoard = ImageIO.read(new File("assets/images/gameBoard.png"));
            pieceMap.put('P', ImageIO.read(new File("assets/images/wPawn.png")));
            pieceMap.put('N', ImageIO.read(new File("assets/images/wKnight.png")));
            pieceMap.put('B', ImageIO.read(new File("assets/images/wBishop.png")));
            pieceMap.put('R', ImageIO.read(new File("assets/images/wRook.png")));
            pieceMap.put('Q', ImageIO.read(new File("assets/images/wQueen.png")));
            pieceMap.put('K', ImageIO.read(new File("assets/images/wKing.png")));
            pieceMap.put('p', ImageIO.read(new File("assets/images/bPawn.png")));
            pieceMap.put('n', ImageIO.read(new File("assets/images/bKnight.png")));
            pieceMap.put('b', ImageIO.read(new File("assets/images/bBishop.png")));
            pieceMap.put('r', ImageIO.read(new File("assets/images/bRook.png")));
            pieceMap.put('q', ImageIO.read(new File("assets/images/bQueen.png")));
            pieceMap.put('k', ImageIO.read(new File("assets/images/bKing.png")));
        } catch (Exception e) {
            log.error("Error loading chess resources", e);
            throw e;
        }

        log.info("Successfully loaded chess resources!");
    }

    public RenderedImage generateBoard(String fen) {
        BufferedImage image = new BufferedImage(gameBoard.getWidth(), gameBoard.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.drawImage(gameBoard, 0, 0, null);

        int x = 40, y = 40;

        for (Character character : fen.toCharArray()) {
            if (Character.isDigit(character))
                x += 60 * Integer.parseInt(character.toString());
            else if (character == '/') {
                x = 40;
                y += 60;
            } else {
                g.drawImage(pieceMap.get(character), x, y, null);
                x += 60;
            }
        }

        return image;
    }

    public InputStream generateBoardInputStream(String fen) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(generateBoard(fen), "png", stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }
}
