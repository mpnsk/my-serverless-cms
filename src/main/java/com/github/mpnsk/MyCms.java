package com.github.mpnsk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

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
                jedis.hset(entityName + ":" + idCursor, key, book.get(key));
            }
            jedis.hset(entityName + "-meta", "id-cursor", String.valueOf(idCursor));
        }
    }
}