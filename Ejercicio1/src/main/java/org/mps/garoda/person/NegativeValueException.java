package org.mps.garoda.person;

/**
 * Auxiliary exception meant to be thrown when a {@link Number} is negative.
 *
 * @author Daniel Garc�a Rodr�guez
 */
public class NegativeValueException extends IllegalArgumentException{
    public NegativeValueException(String errorMessage){
        super(errorMessage);
    }
}
