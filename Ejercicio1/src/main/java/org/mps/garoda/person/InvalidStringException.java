package org.mps.garoda.person;

/**
 * Auxiliary exception meant to be thrown when a {@link String} doesn't meet a certain criteria.
 *
 * @author Daniel García Rodríguez
 */
public class InvalidStringException extends IllegalArgumentException{
    public InvalidStringException(String errorMessage) {
        super(errorMessage);
    }
}
