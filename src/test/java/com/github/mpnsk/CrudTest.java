package com.github.mpnsk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class CrudTest {


    private MyCms myCms;

    @BeforeEach
    void setUp() {
        myCms = new MyCms();
    }

    @Test
    void crud_create() {
        Map<String, Class> metadata = DummyData.bookMetadata();
        myCms.setup(metadata);

        Map<String, String> hobbit = DummyData.bookHobbit();
        var tryToFindHobbit = myCms.CRUD_readByAttribute("title", hobbit.get("title"));
        assertTrue(tryToFindHobbit.isEmpty());

        myCms.CRUD_create(hobbit);
        var foundHobbit = myCms.CRUD_readByAttribute("title", hobbit.get("title")).get();
        assertEquals(hobbit, foundHobbit);


        Map<String, String> fourthWing = DummyData.bookFourthWing();
        var tryToFindFourthWing = myCms.CRUD_readByAttribute("title", fourthWing.get("title"));
        assertTrue(tryToFindFourthWing.isEmpty());

        myCms.CRUD_create(fourthWing);
        var foundFourthWing = myCms.CRUD_readByAttribute("title", fourthWing.get("title")).get();
        assertEquals(fourthWing, foundFourthWing);
    }

}
