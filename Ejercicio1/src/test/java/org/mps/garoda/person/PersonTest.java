package org.mps.garoda.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class {@link Person} and its methods (excluding getters).
 */
class PersonTest {
    private Person person;

    /**
     * Initialization of auxiliary object person.
     */
    @BeforeEach
    void setUp() {
        person = new Person("Steve", 28, "male");
        assertNotNull(person);
    }

    /**
     * Tests if creating a {@link Person} with negative age results in
     * an {@link InvalidAgeException} exception.
     */
    @Test
    void aPersonCantHaveANegativeAge() {
        Random random = new Random();
        int age = random.nextInt(-100, 0);

        assertThrows(InvalidAgeException.class, () -> new Person("test", age, "male"));
        assertThrows(InvalidAgeException.class, () -> new Person("test", age, "female"));
    }

    /**
     * Tests if creating a {@link Person} with an age bigger than a reasonable maximum results in
     * an {@link InvalidAgeException} exception.
     */
    @Test
    void aPersonCantHaveAnAgeTooLarge() {
        Random random = new Random();
        int age = random.nextInt(Person.MAX_AGE, Person.MAX_AGE + 100);

        assertThrows(InvalidAgeException.class, () -> new Person("test", age, "male"));
        assertThrows(InvalidAgeException.class, () -> new Person("test", age, "female"));
    }

    /**
     * Tests if the average ages of a diverse (not empty) group of people are not negative.<br/>
     * <b>Test case:</b>  4 women and 4 men with pseudo-random ages.
     *
     * @see #addWomen(List)
     * @see #addMen(List)
     */
    @Test
    void averageAgesAreNotNegative() {
        List<Person> people = new ArrayList<>(8);
        addWomen(people);
        addMen(people);
        assertFalse(people.isEmpty());
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertFalse(averages[0] < 0);
        assertFalse(averages[1] < 0);
    }

    /**
     * Tests if the male average is 0 when no men are given.<br/>
     * <b>Test case:</b> 4 women and 0 men, with pseudo-random valid ages.
     *
     * @see #addWomen(List)
     */
    @Test
    void averageMaleAgeIsZeroIfNoMen() {
        List<Person> people = new ArrayList<>(4);
        addWomen(people);
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertEquals(0, averages[0]);
    }

    /**
     * Tests if the female average is 0 when no women are given.<br/>
     * <b>Test case:</b> 4 men and 0 women, with pseudo-random valid ages.
     *
     * @see #addMen(List)
     */
    @Test
    void averageFemaleAgeIsZeroIfNoWomen() {
        List<Person> people = new ArrayList<>(4);
        addMen(people);
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertEquals(0, averages[1]);
    }

    /**
     * Auxiliary method to add four men to a list of {@link Person}.
     * The ages are chosen pseudo-randomly.
     *
     * @param people the list of people to add to
     */
    private void addMen(List<Person> people) {
        Random random = new Random();
        people.add(new Person("Jonah", random.nextInt(Person.MAX_AGE), "male"));
        people.add(new Person("Dan", random.nextInt(Person.MAX_AGE), "male"));
        people.add(new Person("Matt", random.nextInt(Person.MAX_AGE), "male"));
        people.add(new Person("Joseph", random.nextInt(Person.MAX_AGE), "male"));
    }

    /**
     * Auxiliary method to add four women to a list of {@link Person}.
     * The ages are chosen pseudo-randomly.
     *
     * @param people the list of people to add to
     */
    private void addWomen(List<Person> people) {
        Random random = new Random();
        people.add(new Person("Claire", random.nextInt(Person.MAX_AGE), "female"));
        people.add(new Person("Dianne", random.nextInt(Person.MAX_AGE), "female"));
        people.add(new Person("Susie", random.nextInt(Person.MAX_AGE), "female"));
        people.add(new Person("Angela", random.nextInt(Person.MAX_AGE), "female"));
    }
}