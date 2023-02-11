package org.mps.garoda.person;

public class EmptyStringException extends IllegalArgumentException{
    public EmptyStringException(String errorMessage) {
        super(errorMessage);
    }
}
