package com.github.mpnsk;

import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public final class Pet {
    String name;
    LocalDate birthDate;

    public Pet(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    Map<String, String> toMap() {
        Map<String, String> map = Map.of(
                "name", name,
                "birthDate", String.valueOf(birthDate));
        return map;
    }

    static Pet fromMap(Map<String, String> map) {
        return new Pet(map.get("name"), LocalDate.parse(map.get("birthDate")));
    }


    public void create(Jedis jedis) {
        jedis.hset("pet:1", this.toMap());
    }

    public static Pet read(Jedis jedis) {
        Map<String, String> map = jedis.hgetAll("pet:1");
        return fromMap(map);
    }

    public void update(Jedis jedis) {
        jedis.hset("pet:1", this.toMap());
    }

    public void delete(Jedis jedis){
        jedis.del("pet:1");
    }

    public String name() {
        return name;
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pet) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.birthDate, that.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate);
    }

    @Override
    public String toString() {
        return "Pet[" +
                "name=" + name + ", " +
                "birthDate=" + birthDate + ']';
    }

}
