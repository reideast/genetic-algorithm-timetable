package net.andreweast.services.ga.geneticalgorithm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DummyTest {

    @BeforeEach
    void setUp() {
        System.out.println("setup");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    void passes() {
        assertTrue(true);
    }

    @Test
    void fails() {
        fail("oh noes!");
    }
}