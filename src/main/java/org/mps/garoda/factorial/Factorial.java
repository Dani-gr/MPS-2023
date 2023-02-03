package org.mps.garoda.factorial;

public class Factorial {
    public int compute(int number){
        int result = 1;
        for (int i = number; i > 1 ; i--) result *= i;
        return result;
    }
}
