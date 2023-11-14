package com.github.mpnsk;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Pet pet1 = new Pet("pet1", LocalDate.now());

        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.flushAll();

                printAllKeys(jedis);
                System.out.println();
                System.out.println("start:");
                pet1.create(jedis);

                Pet read = Pet.read(jedis);
                System.out.println("read = " + read);

                printAllKeys(jedis);
            }
        }
    }

    private static void printAllKeys(Jedis jedis) {
        Set<String> keys = jedis.keys("*");
        System.out.println("keys = " + keys);
        for (String key : keys) {
            Map<String, String> stringStringMap = jedis.hgetAll(key);
            System.out.println("stringStringMap = " + stringStringMap);
        }
    }

    private static void redis_example(Jedis jedis) {
        // Store & Retrieve a simple string
        jedis.set("foo", "bar");
        System.out.println(jedis.get("foo")); // prints bar

        // Store & Retrieve a HashMap
        Map<String, String> hash = new HashMap<>();
        ;
        hash.put("name", "John");
        hash.put("surname", "Smith");
        hash.put("company", "Redis");
        hash.put("age", "29");
        jedis.hset("user-session:123", hash);
        System.out.println(jedis.hgetAll("user-session:123"));
        // Prints: {name=John, surname=Smith, company=Redis, age=29}
    }
}