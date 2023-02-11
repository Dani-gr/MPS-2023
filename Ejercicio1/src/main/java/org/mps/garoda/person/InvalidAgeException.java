package org.mps.garoda.person;

/**
 * Auxiliary exception meant to be thrown when a {@link Number} is negative.
 *
 * @author Daniel Garc�a Rodr�guez
 */
public class InvalidAgeException extends IllegalArgumentException{
    public InvalidAgeException(String errorMessage){
        super(errorMessage);
    }
}
