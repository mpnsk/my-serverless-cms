package com.github.mpnsk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CrudTest {


    private MyCms myCms;

    @BeforeEach
    void setUp() {
        myCms = new MyCms();

        jedis_drop_everything();
    }

    private static void jedis_drop_everything() {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();
//            clean db
            jedis.flushAll();
        }
    }

    @Test
    void crud_create_and_read() {
        Map<String, Class> metadata = DummyData.bookMetadata();
        myCms.setup(metadata);

        Map<String, String> hobbit = DummyData.bookHobbit();
        var tryToFindHobbit = myCms.crud_readByAttribute("title", hobbit.get("title"));
        assertTrue(tryToFindHobbit.isEmpty());

        myCms.CRUD_create(hobbit);
        var foundHobbit = myCms.crud_readByAttribute("title", hobbit.get("title")).get();
        assertEquals(hobbit, foundHobbit);


        Map<String, String> fourthWing = DummyData.bookFourthWing();
        var tryToFindFourthWing = myCms.crud_readByAttribute("title", fourthWing.get("title"));
        assertTrue(tryToFindFourthWing.isEmpty());

        myCms.CRUD_create(fourthWing);
        var foundFourthWing = myCms.crud_readByAttribute("title", fourthWing.get("title")).get();
        assertEquals(fourthWing, foundFourthWing);
    }
    @Test
    void crud_create_and_read_indexed() {
        Map<String, Class> metadata = DummyData.bookMetadata();
        myCms.setup(metadata);

        Map<String, String> hobbit = DummyData.bookHobbit();
        var tryToFindHobbit = myCms.crud_readByIndex(1);
        assertTrue(tryToFindHobbit.isEmpty());

        myCms.CRUD_create(hobbit);
        var foundHobbit = myCms.crud_readByIndex(1);
        assertEquals(hobbit, foundHobbit.get());
    }

    @Test
    void crud_create_update_read() {
        Map<String, Class> metadata = DummyData.bookMetadata();
        myCms.setup(metadata);


        Map<String, String> hobbit = DummyData.bookHobbit();
        var tryToFindHobbit = myCms.crud_readByAttribute("title", hobbit.get("title"));
        assertTrue(tryToFindHobbit.isEmpty());

        myCms.CRUD_create(hobbit);
        var foundHobbit = myCms.crud_readByAttribute("title", hobbit.get("title")).get();
        assertEquals(hobbit, foundHobbit);

        // now update the book
        hobbit.put("release", "1999");
        boolean success = myCms.crud_updateByAttribute("title", hobbit.get("title"), hobbit);
        assertTrue(success);

        var updatedHobbit = myCms.crud_readByAttribute("title", hobbit.get("title")).get();
        assertEquals(updatedHobbit.get("release"), "1999");
    }

    @Test
    void crud_create_delete() {
        Map<String, Class> metadata = DummyData.bookMetadata();
        myCms.setup(metadata);

        Map<String, String> hobbit = DummyData.bookHobbit();
        var tryToFindHobbit = myCms.crud_readByAttribute("title", hobbit.get("title"));
        assertTrue(tryToFindHobbit.isEmpty());

        myCms.CRUD_create(hobbit);
        var foundHobbit = myCms.crud_readByAttribute("title", hobbit.get("title")).get();
        assertEquals(hobbit, foundHobbit);

        boolean success = myCms.crud_deleteByAttribute("title", hobbit.get("title"));
        assertTrue(success);


        tryToFindHobbit = myCms.crud_readByAttribute("title", hobbit.get("title"));
        assertTrue(tryToFindHobbit.isEmpty());
    }
}
