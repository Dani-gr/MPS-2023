package org.mps.plateau;

import org.mps.coverage.ExcludeGeneratedCodeFromCoverage;

public class Plateau {

    @ExcludeGeneratedCodeFromCoverage
    record Pair(int position, int length) {
    }

    @ExcludeGeneratedCodeFromCoverage
    public Plateau() {
    }

    /**
     * Computes the position and length of the longest plateau in the given array.
     * <p>
     * Given an array of integers, a plateau is a non-empty sequence of consecutive elements
     * equal to each other, preceded and followed by an array element strictly smaller than
     * those composing the plateau.
     * <ul>
     * <li>If there are several plateaus of the same length, it returns the first one appearing in the array.</li>
     * <li>If there is no plateau, -1 is returned as position and 0 as length.</li>
     * <li>If there can be no plateau (the array is null or has less than 3 elements) an exception is raised.</li>
     * </ul>
     *
     * @param xs the array whose longest plateau is to be computed
     * @return the position and length of the longest plateau in the given array
     * @throws IllegalArgumentException if there can be no plateau in the given array
     */
    public static Pair longestPlateau(int[] xs) {
        if (xs == null || xs.length < 3) {
            throw new IllegalArgumentException();
        }

        int longestPos = -1;
        int longestLength = 0;
        int i = 0;
        while (i < xs.length - 2) {
            while (i < xs.length - 2 && xs[i] >= xs[i + 1]) {
                i++;
            }
            if (i < xs.length - 2) {
                i++;
                int currentPos = i;
                while (i < xs.length - 1 && xs[i] == xs[i + 1]) {
                    i++;
                }
                if (i < xs.length - 1 && xs[i] > xs[i + 1] && i - currentPos + 1 > longestLength) {
                    longestPos = currentPos;
                    longestLength = i - currentPos + 1;
                } else {
                    System.out.println(); // para comprobar fácilmente la cobertura de ramas
                }
            }
        }

        return new Pair(longestPos, longestLength);
    }
}
