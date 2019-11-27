package xyz.niflheim.stockfish.engine.enums;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.niflheim.stockfish.util.StringUtil.START_FEN;

class QueryTest {

    private Query query;

    @BeforeEach
    void setUp() {
        query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getType() {
        Query query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertEquals(QueryType.Make_Move, query.getType());

        query = new Query.Builder(QueryType.Legal_Moves, START_FEN).build();
        assertEquals(QueryType.Legal_Moves, query.getType());

        query = new Query.Builder(QueryType.Best_Move, START_FEN).build();
        assertEquals(QueryType.Best_Move, query.getType());

        query = new Query.Builder(QueryType.Checkers, START_FEN).build();
        assertEquals(QueryType.Checkers, query.getType());

        assertThrows(IllegalStateException.class, () -> new Query.Builder(null, START_FEN).build());
    }

    @Test
    void getFen() {
        Query query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertEquals(START_FEN, query.getFen());

        query = new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b KQkq - 0 1").build();
        assertEquals("8/8/8/8/8/8/8/8 b KQkq - 0 1", query.getFen());

        query = new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 w kkkk - 10 10").build();
        assertEquals("8/8/8/8/8/8/8/8 w kkkk - 10 10", query.getFen());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8//8/8/8 w kkkk - 10 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 t kkkk - 10 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b tkkk - 10 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b kkkkk - 10 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b kkkk aa 10 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b kkkk a4a5 aa 10").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "8/8/8/8/8/8/8/8 b kkkk a4a5 10 aa").build());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, "hello world").build());

        assertThrows(IllegalStateException.class,
                () -> new Query.Builder(QueryType.Make_Move, null).build());
    }

    @Test
    void getMove() {
        Query query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertNull(query.getMove());
        query = new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a2a4").build();
        assertEquals("a2a4", query.getMove());
        query = new Query.Builder(QueryType.Make_Move, START_FEN).setMove("h1a8").build();
        assertEquals("h1a8", query.getMove());

        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove(null).build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a4").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("A4B4").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("aaaa").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a4a9").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a4v5").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a4a6a").build());
        assertThrows(IllegalArgumentException.class,
                () -> new Query.Builder(QueryType.Make_Move, START_FEN).setMove("a4a4a4").build());

    }

    @Test
    void getDifficulty() {
        query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertEquals(-1, query.getDifficulty());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setDifficulty(10).build();
        assertEquals(10, query.getDifficulty());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setDifficulty(-10).build();
        assertEquals(-10, query.getDifficulty());
    }

    @Test
    void getDepth() {
        query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertEquals(-1, query.getDepth());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setDepth(10).build();
        assertEquals(10, query.getDepth());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setDepth(-10).build();
        assertEquals(-10, query.getDepth());
    }

    @Test
    void getMovetime() {
        query = new Query.Builder(QueryType.Make_Move, START_FEN).build();
        assertEquals(-1, query.getMovetime());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setMovetime(10).build();
        assertEquals(10, query.getMovetime());


        query = new Query.Builder(QueryType.Make_Move, START_FEN).setMovetime(-10).build();
        assertEquals(-10, query.getMovetime());
    }
}