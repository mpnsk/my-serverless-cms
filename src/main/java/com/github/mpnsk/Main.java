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
        System.out.println("Main.main");
        System.out.println("args = " + Arrays.toString(args));

        PetWriter petWriter = new PetWriter();

        Pet kiwy = new Pet("kiwy", LocalDate.now());

        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.flushAll();
                petWriter.write(kiwy, jedis);

                Set<String> keys = jedis.keys("*");
                System.out.println("keys = " + keys);
                for (String key : keys) {
                    Map<String, String> stringStringMap = jedis.hgetAll(key);
                    System.out.println("stringStringMap = " + stringStringMap);
                }
//                redis_example(jedis);
            }
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