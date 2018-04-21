# Stockfish Java

This is a java wrapper for the popular chess engine Stockfish 9.

## Getting Started

To start off, build a StockfishClient using the builder:

```
StockfishClient client = new StockfishClient.Builder()
    .setOption("Threads", Runtime.getRuntime().availableProcessors())
    .setVariant(Variant.DEFAULT)
    .build();
```

You can choose what options to start the stockfish instance with by using the `.setOption(name, value)` command. To see a list of engine parameters check out [Stockfish's website](http://support.stockfishchess.org/kb/advanced-topics/engine-parameters).

## Example Usages

The client is pretty straight forward and can be used either of two ways. You can either query it for specific situations or play an actual game with it.

To query the client for specific situations, you can either use:

```
client.setFen(String FEN);
```

Before each command or use the variants of the command that accepts a FEN parameter:

```
List<String> getCheckers(String fen); //returns a list of positions

String getBestMove(String fen, int skill); //returns a best move in the format of 2 position squares

void setFen(String fen);

String makeMove(String fen, String move); //returns the new FEN after making specified move
```

To play a game with the client, you can use variants of the command that don't accept a FEN parameter. The engine starts off with the default startpos fen:

```
rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
```

Commands such as:

```
List<String> getCheckers(); //returns a list of positions

String getBestMove(int skill); /returns a best move in the format of 2 position squares

String makeMove(String move); //returns the new FEN after making specified move
```

Will use the current FEN stored in the stockfish instance and will update that fen every time a command is made. Take note, however, that if you query an illegal command, the instance will crash.

To grab the FEN stored in the instance or set the FEN, use commands:

```
client.getFen();

client.setFen(FEN);
```

Setting an illegal FEN, like querying an illegal command, will crash the instance.

As a quality of life, I have included a board generator that generates a rendered image or an input stream. You can use it however you like or just jack my assets.

```
client.generateBoard(String fen); //RenderedImage

client.streamBoard(String fen); //InputStream
```

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Typesafe Config](https://github.com/lightbend/config) - Used to import settings
* [Stockfish 9](https://stockfishchess.org/) - Underlying chess engine

## Contributing

This repo is pretty messy as of right now and any help cleaning it up would be very helpful. I originally wrote this code for [Automata Bot](http://automata-bot.xyz) but sloppily threw it together here for others to use as well.

Please read [CONTRIBUTING.md](/CONTRIBUTING.md) for details on how to contribute to this project.

## Authors

* **David Wang** - *Initial work* - [NiflheimDev](https://github.com/NiflheimDev)

Currently there are no other contributors, so feel free to send in a pull request and I can add you.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
