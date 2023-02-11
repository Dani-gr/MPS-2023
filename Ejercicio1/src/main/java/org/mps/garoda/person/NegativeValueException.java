package org.mps.garoda.person;

public class NegativeValueException extends IllegalArgumentException{
    public NegativeValueException(String errorMessage){
        super(errorMessage);
    }
}
