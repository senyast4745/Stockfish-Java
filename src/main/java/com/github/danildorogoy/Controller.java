package com.github.danildorogoy;

import com.github.danildorogoy.models.ChessPiece;
import com.github.danildorogoy.models.Square;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.QueryType;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class Controller implements Initializable {
    private boolean isFirstClick = false;
    private String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private String move = "";
    private static final Log log = LogFactory.getLog(Main.class);
    private Map<String, Square> map = new HashMap<>(64);

    @FXML
    private GridPane gridpane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                map.put(i + "" + j, new Square((i + j) % 2 == 0, ((char) ('a' + i) + "" + (8 - j))));
            }
        }
        addGridEvent();
    }

    private void mouseEntered(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        colIndex = colIndex == null ? 0 : colIndex;
        rowIndex = rowIndex == null ? 0 : rowIndex;

        log.info(event);
        if (isFirstClick) {
            move += map.get(colIndex + "" + rowIndex).getCoord();
            isFirstClick = false;
            CompletableFuture<String> resultFuture = new CompletableFuture<>();
            log.info(move);
            Main.client.submit(new Query.Builder(QueryType.Make_Move, fen)
                    .setMove(move).build(), resultFuture::complete);

            try {
                fen = resultFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
            resultFuture = new CompletableFuture<>();
            log.info(fen);
            Main.client.submit(new Query.Builder(QueryType.Best_Move, fen)
                    .build(), resultFuture::complete);
            try {
                move = resultFuture.get();
                resultFuture.cancel(false);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
            resultFuture = new CompletableFuture<>();
            log.info(move);
            Main.client.submit(new Query.Builder(QueryType.Make_Move, fen)
                    .setMove(move).build(), resultFuture::complete);
            move = "";

            try {
                fen = resultFuture.get();
                resultFuture.cancel(false);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
            log.info(fen);
            display(fen);
        } else {
            move = map.get(colIndex + "" + rowIndex).getCoord();
            isFirstClick = true;
        }
        log.info(String.format("Mouse entered cell [%d, %d]%n", colIndex, rowIndex));
    }

    private void addGridEvent() {
        gridpane.getChildren().forEach(item -> {
           /* Integer colIndex = GridPane.getColumnIndex(item);
            Integer rowIndex = GridPane.getRowIndex(item);
            colIndex = colIndex == null ? 0 : colIndex;
            rowIndex = rowIndex == null ? 0 : rowIndex;
            Integer finalColIndex = colIndex;
            Integer finalRowIndex = rowIndex;
            for (String[] a : parserFen(fen)) {
                log.info(Arrays.toString(a));
            }

            String[][] board = parserFen(fen);

            Platform.runLater(() -> gridpane.add(
                    new ImageView(ChessPiece.getChessPiece(board[finalRowIndex][finalColIndex]).getImg()),
                    finalColIndex, finalRowIndex));*/
            item.setOnMouseClicked(this::mouseEntered);
        });
        initDisplay(fen);
    }

    private void display(String fen) {
        String[][] board = parserFen(fen);
        for (String[] a : parserFen(fen)) {
            log.info(Arrays.toString(a));
        }
        gridpane.getChildren().forEach(item -> {
            Integer colIndex = GridPane.getColumnIndex(item);
            Integer rowIndex = GridPane.getRowIndex(item);
            colIndex = colIndex == null ? 0 : colIndex;
            rowIndex = rowIndex == null ? 0 : rowIndex;
            int finalColIndex = colIndex;
            int finalRowIndex = rowIndex;


            String url = ChessPiece.getChessPiece(board[finalRowIndex][finalColIndex]).getImg();
            if (!url.isEmpty()) {
                Platform.runLater(() -> gridpane.add(
                        new ImageView(url),
                        finalColIndex, finalRowIndex));
            } else {
                Platform.runLater(() -> {

                    if (item instanceof ImageView) {
                        gridpane.getChildren().remove(item);
                    }
                });
            }
        });
    }


    private void initDisplay(String fen) {
        String[][] board = parserFen(fen);
        for (String[] a : parserFen(fen)) {
            log.info(Arrays.toString(a));
        }
        gridpane.getChildren().forEach(item -> {
            Integer colIndex = GridPane.getColumnIndex(item);
            Integer rowIndex = GridPane.getRowIndex(item);
            colIndex = colIndex == null ? 0 : colIndex;
            rowIndex = rowIndex == null ? 0 : rowIndex;
            int finalColIndex = colIndex;
            int finalRowIndex = rowIndex;


            String url = ChessPiece.getChessPiece(board[finalRowIndex][finalColIndex]).getImg();
            if (!url.isEmpty()) {
                Platform.runLater(() -> gridpane.add(
                        new ImageView(url),
                        finalColIndex, finalRowIndex));
            }
        });
    }

    private String[][] parserFen(String fen) {
        fen = fen.substring(0, fen.indexOf(' '));
        String[] pos = fen.split("/");
        String[][] board = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0, t = 0; j < pos[i].length() && t < 8; j++, t++) {
                if (Character.isDigit(pos[i].toCharArray()[j])) {
                    int temp = pos[i].toCharArray()[j] - '0';
                    for (int k = 0; k < temp; k++) {
                        board[i][t + k] = "none";
                    }
                    t += temp - 1;
                    continue;
                }

                board[i][t] = String.valueOf(pos[i].toCharArray()[j]);

            }
        }
        return board;
    }

}
