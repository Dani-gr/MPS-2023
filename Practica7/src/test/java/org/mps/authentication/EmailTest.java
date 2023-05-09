package org.mps.authentication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EmailTest {
    @Test
    public void validateReturnsTrueIfTheEmailIsValid() {
        assertTrue(new Email("mps@uma.es").validate());
    }

    @Test
    public void validateReturnsFalseIfThereAreTwoAts() {
        assertFalse(new Email("c@h@uma.es").validate());
    }

    @Test
    public void validateReturnsFalseIfThereIsNoAt() {
        assertFalse(new Email("mps").validate());
    }

    @Test
    public void validateReturnsFalseIfThereIsOnlyOneSubdomain() {
        assertFalse(new Email("mps@umaes").validate());
    }

    @Test
    public void validateReturnsTrueIfTheMailboxHasValidChars() {
        assertTrue(new Email("mp0G.g-_d@uma.es").validate());
    }

    @Test
    public void validateReturnsFalseIfThereAreTwoDotsTogether() {
        assertFalse(new Email("mp0G..g-_d@uma.es").validate());
    }

    @Test
    public void validateReturnsFalseIfOneSubdomainIsEmpty() {
        assertFalse(new Email("mps@uma..es").validate());
    }

    @Test
    public void validateReturnsTrueIfDashIsUsedInASubdomain() {
        assertTrue(new Email("mps@uma-es.es").validate());
    }

    @Test
    public void validateReturnsFalseIfDashIsAtTheEndOfSubdomain() {
        assertFalse(new Email("mps@uma-.es").validate());
    }

    @Test
    public void validateReturnsFalseIfDashIsAtTheStartOfSubdomain() {
        assertFalse(new Email("mps@-uma.es").validate());
    }
}
