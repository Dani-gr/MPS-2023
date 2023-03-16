package org.mps.garoda.factorial;

import java.math.BigInteger;

public class Factorial {
    public int compute(int number){
        if(number < 0) throw new NegativeValueException("The value " + number + " is negative");
        int result = 1;
        for (int i = number; i > 1 ; i--) result *= i;
        return result;
    }
	public BigInteger computeBigValue(int number) {
		if (number < 0) throw new NegativeValueException("The value " + number + " is negative");
		BigInteger result;
		result = BigInteger.ONE;
		for (int i = number; i > 1 ; i--) result = result.multiply(BigInteger.valueOf(number));
		return result;
  }
}
