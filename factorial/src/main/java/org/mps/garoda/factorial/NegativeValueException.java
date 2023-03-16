package org.mps.garoda.factorial;

public class NegativeValueException extends RuntimeException{
	public NegativeValueException(String error){
		super(error);
	}
}
