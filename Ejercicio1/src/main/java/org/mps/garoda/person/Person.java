package org.mps.garoda.person;

import java.util.Locale;

/**
 * Class representing a person with a name, age and gender.
 *
 * @author Daniel García Rodríguez
 */
public class Person {
    private final String name;
    private final int age;
    private final String gender; //male, female

    /**
     * Constructs a person with a name, age and gender.
     *
     * @param name   the name of the person
     * @param age    the age of the person
     * @param gender the gender of the person
     */
    public Person(String name, int age, String gender) {
        if (name.isEmpty()) throw new EmptyStringException("A person's name can't be empty.");
        if (age < 0) throw new NegativeValueException("A person's age can't be negative.");
        if (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")))
            throw new EmptyStringException("A person's gender must be 'male' or 'female'");

        this.name = name.toLowerCase(Locale.ROOT);
        this.age = age;
        this.gender = gender.toLowerCase(Locale.ROOT);
    }

    public String name() {
        return name;
    }

    public int age() {
        return age;
    }

    public String gender() {
        return gender;
    }
}
