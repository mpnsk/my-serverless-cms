package com.github.mpnsk;

import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PetWriter {
    void write(Pet pet, Jedis jedis) {
        System.out.println("pet = " + pet + ", jedis = " + jedis);

        Map<String, String> map = Map.of(
                "name", pet.name(),
                "birthdate", String.valueOf(pet.birthDate()));

        jedis.hset("pet:1", map);
    }
}
