package com.github.mpnsk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        myCms.CRUD_create(hobbit);

        Map<String, String> fourthWing = DummyData.bookFourthWing();
        myCms.CRUD_create(fourthWing);

        Map<String, String> found = myCms.CRUD_readByAttribute("title", hobbit.get("title"));
        System.out.println("found = " + found);
    }

}
