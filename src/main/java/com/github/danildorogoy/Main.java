package com.github.danildorogoy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.StockfishClient;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.QueryType;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    private static final Log log = LogFactory.getLog(Main.class);

    public static StockfishClient client = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        try {
            client = new StockfishClient.Builder().build();
        } catch (StockfishInitException e) {
            log.error(e);
            if (client != null) {
                client.close();
            }
            clone();

        }
    }

    @Override
    public void start(Stage stage) throws IOException {

        log.info("Hello World");

        AtomicReference<String> fen = new AtomicReference<>();

/*
        Button btn = new Button();
        Button[] field = new Button[8];
        for (int i = 0; i < 8; i++) {
            field[i] = new Button();
        }
        btn.setText("Click!");*/
        Query query = new Query
                .Builder(QueryType.Make_Move,
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").setMove("e2e4")
                .build();

       /* btn.setOnAction(event -> {
            CompletableFuture<String> resultFuture = new CompletableFuture<>();
            client.submit(query, result -> {
                log.info(result);
                fen.set(result);
                resultFuture.complete(result);
            });
            try {
                btn.setText(resultFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                stop();
            }
        });


        Text text = new Text(fen.get());
        text.setLayoutY(80);    // установка положения надписи по оси Y
        text.setLayoutX(100);   // установка положения надписи по оси X

        Group group = new Group(field);

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("First Application");
        stage.setWidth(300);
        stage.setHeight(250);
        stage.show();*/
        Parent root = null;

        root = FXMLLoader.load(Main.class.getClassLoader().getResource("test.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Hello JavaFX");
        stage.setWidth(stage.getMaxWidth());
        stage.setHeight(stage.getMaxHeight());

        stage.show();
    }

    @Override
    public void stop() {
        log.info("Close");
        if (client != null) {
            client.close();
        }
    }

}
