package org.mps.garoda.person;

import java.util.List;
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
        if (name.isBlank()) throw new InvalidStringException("A person's name can't be empty.");
        if (age < 0) throw new NegativeValueException("A person's age can't be negative.");
        if (gender.isBlank() || !(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")))
            throw new InvalidStringException("A person's gender must be 'male' or 'female'");

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

    /**
     * Computes the average age of male and female persons in a {@link List list} and returns the result in
     * an {@link java.lang.reflect.Array array} of two elements (the first element is the male mean age and
     * the second one is the female mean age).
     *
     * @param persons the list of people whose age's average to compute
     * @return an array with the mean ages (the first element being the male mean age, and the second one,
     * the female mean age.
     */
    public double[] averageAgePerGender(List<Person> persons) {
        //TODO
        return null;
    }
}
