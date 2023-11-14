package com.github.mpnsk;

import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.Map;

public record Pet(String name, LocalDate birthDate) {

    Map<String, String> toMap() {
        Map<String, String> map = Map.of(
                "name", name,
                "birthDate", String.valueOf(birthDate));
        return map;
    }

    static Pet fromMap(Map<String,String> map) {
        return new Pet(map.get("name"), LocalDate.parse(map.get("birthDate")));
    }


    public void create(Jedis jedis) {
        jedis.hset("pet:1", this.toMap());
    }

    public static Pet read(Jedis jedis){
        Map<String, String> map = jedis.hgetAll("pet:1");
        return fromMap(map);
    }
}
