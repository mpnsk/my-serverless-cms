package com.github.mpnsk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MyCms {

    public void setup(Map<String, Class> metadata) {

        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

//            clean db
            jedis.flushAll();

            String entityname = "book";
            for (String key : metadata.keySet()) {
                Class datatype = metadata.get(key);

                jedis.hset(entityname + "-meta", key, datatype.getSimpleName());

            }
            jedis.hset(entityname + "-meta", "id-cursor", "0");


        }
    }

    public void CRUD_create(Map<String, String> book) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String entityName = "book";
            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            final int idCursor = Integer.parseInt(idCursorString) + 1;

            for (String key : book.keySet()) {
                String value = book.get(key);
                jedis.hset(entityName + ":" + idCursor, key, value);
            }
            jedis.hset(entityName + "-meta", "id-cursor", String.valueOf(idCursor));
        }
    }

    public Optional<Map<String, String>> CRUD_readByAttribute(String key, String value) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String entityName = "book";
            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            final int idCursor = Integer.parseInt(idCursorString) + 1;
            List<Integer> nonMatchingIds = new ArrayList<>();
            Integer foundId = null;
            for (int i = 1; i < idCursor; i++) {
                String entityKey = entityName + ":" + i;
                String actualValue = jedis.hget(entityKey, key);
                if (actualValue.equals(value)) {
                    System.out.println("id = " + i);
                    System.out.println("actualValue = " + actualValue);
                    System.out.println("found matching entity!");
                    foundId = i;
                } else {
                    nonMatchingIds.add(i);
                    System.out.println("actualValue = " + actualValue + ", but expected " + value);
                }
            }

            System.out.println("non matching: "+nonMatchingIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));

            if (foundId != null) {
                Map<String, String> entity = jedis.hgetAll(entityName + ":" + foundId);
                System.out.println("entity = " + entity);
                return Optional.of(entity);
            }else {
                System.out.println("no entity found");
            }
        }
        return Optional.empty();
    }
}