package org.mps.garoda.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private Person person;
    @BeforeEach
    void setUp() {
        person = new Person("Steve",28,"male");
        assertNotNull(person);
    }

    @Test
    void averageAgesAreNotNegative() {
        List<Person> people = new ArrayList<>(8);
        addWomen(people);
        addMen(people);
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertFalse(averages[0] < 0);
        assertFalse(averages[1] < 0);
    }

    @Test
    void averageMaleAgeIsZeroIfNoMen() {
        List<Person> people = new ArrayList<>(4);
        addWomen(people);
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertEquals(0, averages[0]);
    }

    @Test
    void averageFemaleAgeIsZeroIfNoWomen() {
        List<Person> people = new ArrayList<>(4);
        addMen(people);
        double[] averages = person.averageAgePerGender(people);

        assertNotNull(averages);
        assertEquals(0, averages[1]);
    }

    private void addMen(List<Person> people){
        Random random = new Random();
        people.add(new Person("Jonah", random.nextInt(120), "male"));
        people.add(new Person("Dan", random.nextInt(120), "male"));
        people.add(new Person("Matt",random.nextInt(120),"male"));
        people.add(new Person("Joseph",random.nextInt(120),"male"));
    }

    private void addWomen(List<Person> people){
        Random random = new Random();
        people.add(new Person("Claire",random.nextInt(120),"female"));
        people.add(new Person("Dianne",random.nextInt(120),"female"));
        people.add(new Person("Susie",random.nextInt(120),"female"));
        people.add(new Person("Angela",random.nextInt(120),"female"));
    }
}