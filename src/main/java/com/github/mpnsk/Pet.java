package com.github.mpnsk;

import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public record Pet(String name, LocalDate birthDate) {

    Map<String, String> toMap() {
        Map<String, String> map = Map.of(
                "name", name,
                "birthDate", String.valueOf(birthDate));
        return map;
    }

    public void create(Jedis jedis) {
        jedis.hset("pet:1", this.toMap());
    }
}
