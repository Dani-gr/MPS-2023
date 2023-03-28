package org.mps.plateau;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("LineCoverage")
class PlateauLineCoverageTest {
    private int[] array;

    @Test
    @DisplayName("Given a null array, when calling longestPlateau() then an IllegalArgumentException is thrown.")
    void nullArray() {
        array = null;

        assertThrows(IllegalArgumentException.class, () -> Plateau.longestPlateau(array));
    }

    @Test
    @DisplayName("Given a non-null array [1 0 3 3 0 5 2], when calling longestPlateau() " +
            "then the first plateau (position 2 and length 3) is returned as a Pair.")
    void nonNullArray() {
        array = new int[]{1, 0, 4, 4, 4, 0, 5, 2};

        Plateau.Pair expected = new Plateau.Pair(2, 3);
        assertEquals(expected, Plateau.longestPlateau(array));
    }
}