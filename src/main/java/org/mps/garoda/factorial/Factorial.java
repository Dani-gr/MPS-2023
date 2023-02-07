package org.mps.garoda.factorial;

public class Factorial {
    public int compute(int number){
        if(number < 0) throw new NegativeValueException();
        int result = 1;
        for (int i = number; i > 1 ; i--) result *= i;
        return result;
    }
}
