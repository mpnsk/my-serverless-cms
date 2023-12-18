package com.github.mpnsk;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MyCms {

    public void setup(String entityName, Map<String, Class> metadata) {

        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            for (String key : metadata.keySet()) {
                Class datatype = metadata.get(key);

                jedis.hset(entityName + "-meta", key, datatype.getSimpleName());

            }
            jedis.hset(entityName + "-meta", "id-cursor", "0");


        }
    }

    public void CRUD_create(String entityName, Map<String, String> book) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            int idCursor = Integer.parseInt(idCursorString);
            idCursor++; // we create a new entry so the cursor has to shift

            for (String key : book.keySet()) {
                String value = book.get(key);
                jedis.hset(entityName + ":" + idCursor, key, value);
            }
            jedis.hset(entityName + "-meta", "id-cursor", String.valueOf(idCursor));
        }
    }

    public Optional<Map<String, String>> crud_readByAttribute(String entityName, String key, String value) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            if (idCursorString == null) {
                log.error("no cursor found");
                return Optional.empty();
            }
            final int idCursor = Integer.parseInt(idCursorString);
            List<Integer> nonMatchingIds = new ArrayList<>();
            Integer foundId = null;
            for (int i = 1; i < idCursor; i++) {
                String entityKey = entityName + ":" + i;
                String actualValue = jedis.hget(entityKey, key);
                if (value.equals(actualValue)) {
                    log.debug("id = " + i);
                    log.debug("actualValue = " + actualValue);
                    log.debug("found matching entity!");
                    foundId = i;
                } else {
                    nonMatchingIds.add(i);
                    log.debug("actualValue = " + actualValue + ", but expected " + value);
                }
            }

            log.debug("non matching: " + nonMatchingIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));

            if (foundId != null) {
                Map<String, String> entity = jedis.hgetAll(entityName + ":" + foundId);
                log.debug("entity = " + entity);
                return Optional.of(entity);
            } else {
                log.debug("no entity found");
            }
        }
        return Optional.empty();
    }

    public boolean crud_updateByAttribute(String key, String value, Map<String, String> updatedEntity) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String entityName = "book";
            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            if (idCursorString == null) {
                log.error("no cursor found");
                return false;
            }
            final int idCursor = Integer.parseInt(idCursorString);
            List<Integer> nonMatchingIds = new ArrayList<>();
            Integer foundId = null;
            for (int i = 1; i <= idCursor; i++) {
                String entityKey = entityName + ":" + i;
                String actualValue = jedis.hget(entityKey, key);
                if (value.equals(actualValue)) {
                    log.debug("id = " + i);
                    log.debug("actualValue = " + actualValue);
                    log.debug("found matching entity!");
                    foundId = i;
                } else {
                    nonMatchingIds.add(i);
                    log.debug("actualValue = " + actualValue + ", but expected " + value);
                }
            }

            log.debug("non matching: " + nonMatchingIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));

            if (foundId != null) {
//                Map<String, String> entity = jedis.hgetAll(entityName + ":" + foundId);
                jedis.hset(entityName + ":" + foundId, updatedEntity);
                return true;
            } else {
                log.debug("no entity found");
            }
        }
        return false;
    }

    public boolean crud_deleteByAttribute(String entityName, String key, String value) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String idCursorString = jedis.hget(entityName + "-meta", "id-cursor");
            if (idCursorString == null) {
                log.error("no cursor found");
                return false;
            }
            final int idCursor = Integer.parseInt(idCursorString) + 1;
            List<Integer> nonMatchingIds = new ArrayList<>();
            Integer foundId = null;
            for (int i = 1; i < idCursor; i++) {
                String entityKey = entityName + ":" + i;
                String actualValue = jedis.hget(entityKey, key);
                if (actualValue.equals(value)) {
                    log.debug("id = " + i);
                    log.debug("actualValue = " + actualValue);
                    log.debug("found matching entity!");
                    foundId = i;
                } else {
                    nonMatchingIds.add(i);
                    log.debug("actualValue = " + actualValue + ", but expected " + value);
                }
            }

            log.debug("non matching: " + nonMatchingIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));

            if (foundId != null) {
                jedis.del(entityName + ":" + foundId);
                return true;
            } else {
                log.debug("no entity found");
            }
        }
        return false;
    }

    public Optional<Map<String, String>> crud_readByIndex(int index) {
        System.out.println("index = " + index);
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            Jedis jedis = pool.getResource();

            String entityName = "book";
            String entityKey = entityName + ":" + index;
            try {
                var result = jedis.hgetAll(entityKey);
                log.debug("result = " + result);
                if (result.equals(Map.of())) {
                    log.error("no entity found");
                    return Optional.empty();
                }
                return Optional.of(result);
            } catch (Exception e) {
                log.error(e.getMessage());
                return Optional.empty();
            }
        }
    }
}