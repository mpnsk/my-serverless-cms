package com.github.mpnsk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MetadataTest {


    private MyCms myCms;

    @BeforeEach
    void setUp() {
        myCms = new MyCms();
    }

    // creating metadata:
    // what i supply: maps as specification of my entities
    // what i want: the maps to be persisted as metadata in redis
    // in a way that i can later put and get pojos as map or json using the metadata

    @Test
    void create_metadata_book() {

    }

    Map<String, Class> metaDataBook() {
        HashMap<String, Class> map = new HashMap<>();
        map.put("title", String.class);
        map.put("release", String.class);
        map.put("description", String.class);
        return map;
    }

    Map<String, String> inputBook() {
        Map<String, String> map = new HashMap<>();
        map.put("title", "the hobbit");
        map.put("release", "1937");
        map.put("description", """
                Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely travelling further than the pantry of his hobbit-hole in Bag End.
                                
                But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon…
                """);
        return map;
    }
}
