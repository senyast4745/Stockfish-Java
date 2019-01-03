[license]: https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg?style=flat-square
[FAQ]: https://img.shields.io/badge/Wiki-FAQ-blue.svg?style=flat-square
[![license]](https://github.com/NiflheimDev/Stockfish-Java/tree/master/LICENSE)
[![FAQ]](https://github.com/NiflheimDev/Stockfish-Java/wiki)

<img align="right" src="https://i.imgur.com/D3DIZQH.png" height="250" width="250">

# Stockfish Java
**Stockfish Java** is an open source asynchronous high level wrapper for the popular uci chess engine [Stockfish 10](https://stockfishchess.org/)

## Quick Start
To start, create a Stockfish Client instance:
```java
StockfishClient client = new StockfishClient.Builder()
        .setInstances(4)
        .setVariant(Variant.BMI2)
        .build();
```
This library allows multiple Stockfish instances to run concurrently to lessen the impact of thread blocking chess computation.
By default, the client only creates one instance, but we can override that by setting the number in the builder. There are also 
multiple variants of Stockfish. In the builder, we can specify BMI2, POPCNT (Windows only), or MODERN (Linux only). A general rule
of thumb is to go with BMI2 first and if it crashes or is incompatible with your system, use POPCNT/MODERN depending on your operating
system. If variant is not set in the builder, we default to the regular Stockfish binary.

After creating the client, we can define a query for the client to compute:
```java
Query query = new Query.Builder(QueryType.Legal_Moves)
        .setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
        .build();
```
More on queries can be found in the wiki, but for now we're just going to submit the query to the client:
```java
client.submit(query, result -> {
    // Do something with the result
})
```
Using a callback, we grab the result and can use it with a lambda.

The included [tester](https://github.com/NiflheimDev/Stockfish-Java/blob/master/src/main/java/xyz/niflheim/stockfish/StockfishTester.java) shows some more example usage of the Stockfish Java library.

## Documentation
Information regarding Stockfish Java as well as full usage of the client is detailed in the [wiki](https://github.com/NiflheimDev/Stockfish-Java/wiki). As  this is a constantly evolving project, the wiki will receive frequent updates and will have the most up to date documentation.

## Contributors
**Owners and Developers**
* [NiflheimDev](https://github.com/NiflheimDev)
* [Kirbyquerby](https://github.com/Kirbyquerby)

**Other Contributors**
* This could be you :)

## Dependencies
* [Stockfish 10](https://stockfishchess.org/)
* [Logback Classic](https://logback.qos.ch/)