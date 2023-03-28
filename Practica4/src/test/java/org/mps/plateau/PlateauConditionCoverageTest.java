package org.mps.plateau;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Tag("ConditionCoverage")
class PlateauConditionCoverageTest {
    private int[] array;
    @Test
    @DisplayName("Given a null array, when calling longestPlateau() then an IllegalArgumentException is thrown.")
    void nullArray() {
        array = null;

        assertThrows(IllegalArgumentException.class, () -> Plateau.longestPlateau(array));
    }

    @Test
    @DisplayName("Given a small array with less than 3 elements, when calling longestPlateau() then an IllegalArgumentException is thrown.")
    void smallArray() {
        array = new int[]{1};

        assertThrows(IllegalArgumentException.class, () -> Plateau.longestPlateau(array));
    }

    @Test
    @DisplayName("Given a non-null array [1 0 4 4 4 0 7 8 0 2 2 2] with an \"unclosed\" plateau at the end, " +
            "when calling longestPlateau() then the first plateau (position 2 and length 3) is returned as a Pair.")
    void nonNullArrayWithUnfinishedPlateauAtTheEnd() {
        array = new int[]{1, 0, 4, 4, 4, 0, 7, 8, 0, 2, 2, 2};

        Plateau.Pair expected = new Plateau.Pair(2, 3);
        assertEquals(expected, Plateau.longestPlateau(array));
    }

    @Test
    @DisplayName("Given a non-null array [1 0 4 4 4 0 7 8 0 2 3 4] with an \"unclosed\" plateau at the end, " +
            "when calling longestPlateau() then the first plateau (position 2 and length 3) is returned as a Pair.")
    void nonNullArrayWithAscendingOrderAtTheEnd() {
        array = new int[]{5, 4, 3, 2, 1};

        Plateau.Pair expected = new Plateau.Pair(-1, 0);
        assertEquals(expected, Plateau.longestPlateau(array));
    }
}
