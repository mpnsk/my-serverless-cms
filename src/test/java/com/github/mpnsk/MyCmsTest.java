package com.github.mpnsk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MyCmsTest {


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
    void metadata_create() {
        Map<String, Class> metadata = bookMetadata();
        System.out.println("metadata = " + metadata);
        myCms.setup(metadata);
    }

    @Test
    void metadata_input() {
        Map<String, Class> metadata = bookMetadata();
        myCms.setup(metadata);

        Map<String, String> hobbit = bookHobbit();
        myCms.CRUD_create(hobbit);

        Map<String, String> fourthWing = bookFourthWing();
        myCms.CRUD_create(fourthWing);
    }

    Map<String, Class> bookMetadata() {
        HashMap<String, Class> map = new HashMap<>();
        map.put("title", String.class);
        map.put("release", String.class);
        map.put("description", String.class);
        return map;
    }

    Map<String, String> bookHobbit() {
        Map<String, String> map = new HashMap<>();
        map.put("title", "the hobbit");
        map.put("release", "1937");
        map.put("description", """
                Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely travelling further than the pantry of his hobbit-hole in Bag End.
                                
                But his contentment is disturbed when the wizard, Gandalf, and a company of thirteen dwarves arrive on his doorstep one day to whisk him away on an unexpected journey ‘there and back again’. They have a plot to raid the treasure hoard of Smaug the Magnificent, a large and very dangerous dragon…
                """);
        return map;
    }

    Map<String,String> bookFourthWing(){
        HashMap<String, String> map = new HashMap<>();
        map.put("title", "Fourth Wing");
        map.put("release", "2023");
        map.put("description", """
                Rebecca loves military heroes and has been blissfully married to hers for over twenty years. She's the mother of six children, and is currently surviving the teenage years with three of her four hockey-playing sons. When she's not writing, you can find her at the hockey rink or sneaking in some guitar time while guzzling coffee. She and her family live in Colorado with their stubborn English bulldogs, two feisty chinchillas, and a Maine Coon kitten named Artemis, who rules them all.
                """);
        return map;
    }
}
