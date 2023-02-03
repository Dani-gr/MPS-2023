package org.mps.garoda.factorial;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h2>Test cases</h2>
 * <ol>
 *     <li>factorial 0 -> 1</li>
 *     <li>factorial 1 -> 1</li>
 *     <li>factorial 2 -> 2</li>
 *     <li>factorial n -> n * factorial (n-1)</li>
 * </ol>
 */
class FactorialTest {

    private Factorial factorial;

    @BeforeEach
    void setup(){
        factorial = new Factorial();
    }

    @AfterEach
    void shutdown(){
        factorial = null;
    }

    @Test
    void factorialOfZeroIsOne(){
        assertEquals(1,factorial.compute(0));
    }

    @Test
    void factorialOfOneIsOne(){
        assertEquals(1,factorial.compute(1));
    }

    @Test
    void factorialOfTwoIsTwo(){
        assertEquals(2,factorial.compute(2));
    }
}