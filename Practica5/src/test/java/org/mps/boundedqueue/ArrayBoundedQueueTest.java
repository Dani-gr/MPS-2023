package org.mps.boundedqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * TODO create comment
 */
class ArrayBoundedQueueTest {

    private ArrayBoundedQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayBoundedQueue<>(3);
    }

    @Nested
    @DisplayName("Tests for invalid arguments:")
    class InvalidArguments {
        @SuppressWarnings("JUnitTestMethodWithNoAssertions") //TODO: Remove
        @Test
        @DisplayName("Given a non-positive size when creating a queue, an IllegalArgumentException will be thrown.")
        void nonPositiveSizeThrowsException() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> queue = new ArrayBoundedQueue<>(0))
                    .withMessage("ArrayBoundedException: capacity must be positive");
        }

        @Test
        @DisplayName("Given a not-full queue, when putting a null element, an IllegalArgumentException will be thrown.")
        void puttingNullValueThrowsException() {
            assertThat(queue.isFull()).as("The queue should not be full").isFalse();
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> queue.put(null))
                    .withMessage("put: element cannot be null");
        }
    }

    @Test
    @DisplayName("Given a non-empty queue, when the queue is iterated, only not-null elements are iterated through.")
    void onlyNonNullElementsWhenIterating() {
        // Setup [1 - 9] (9 -> 1)
        queue.put(4);
        queue.put(7);
        queue.put(9);
        queue.get();
        queue.get();
        queue.put(1);

        assertThat(queue.isEmpty())
                .as("The queue should not be empty")
                .isFalse();
        var iterator = queue.iterator();
        assertThat(iterator)
                .as("The iterator should not be null")
                .isNotNull();
        iterator.forEachRemaining(element -> assertThat(element)
                .as("All elements iterated through should not be null")
                .isNotNull());
    }

    @Nested
    @DisplayName("Given an empty queue")
    class emptyQueue {
        @Test
        @DisplayName("when the queue is iterated, there's no elements to iterate through.")
        void hasNoElementsInTheIterator() {
            assertThat(queue.isEmpty())
                    .as("The queue should be created empty")
                    .isTrue();
            var iterator = queue.iterator();
            assertThat(iterator)
                    .as("The iterator should not be null")
                    .isNotNull();
            assertThat(iterator.hasNext())
                    .as("The iterator should not have any elements to iterate through.")
                    .isFalse();
        }

        @Test
        @DisplayName("when calling the method get, an EmptyBoundedQueueException is thrown.")
        void getFails() {
            assertThat(queue.isEmpty())
                    .as("The queue should be created empty")
                    .isTrue();
            assertThatExceptionOfType(EmptyBoundedQueueException.class)
                    .isThrownBy(() -> queue.get())
                    .withMessage("get: empty bounded queue");
        }
    }

    @Nested
    @DisplayName("Given an non-empty, non-full queue")
    class nonEmptyNorFullQueue {
        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(queue, "first", 1);
            ReflectionTestUtils.setField(queue, "nextFree", 1);
            queue.put(1);
        }

        @Test
        @DisplayName("when the queue is iterated, the number of elements to iterate through is less than the buffer's size.")
        void hasOnlyQueueElementsInTheIterator() {
            assertThat(queue.isEmpty())
                    .as("The queue should not be empty")
                    .isFalse();
            assertThat(queue.isFull())
                    .as("The queue should not be full")
                    .isFalse();

            var iterator = queue.iterator();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(iterator)
                    .as("The iterator should not be null").isNotNull();
            assertThat(buffer)
                    .as("The buffer should not be null").isNotNull();

            int numElements = 0;
            while (iterator.hasNext()) {
                iterator.next();
                numElements++;
            }
            assertThat(numElements)
                    .as("The iterator should iterate through less elements than the buffer's size.")
                    .isLessThan(buffer.length);
        }
    }

    @Nested
    @DisplayName("Given a full queue")
    class fullQueue {
        @BeforeEach
        void setUp() {
            queue.put(1);
            queue.put(2);
            queue.put(3);
        }

        @Test
        @DisplayName("when the queue is iterated, the number of elements to iterate through equals the buffer's size.")
        void hasAllElementsInTheIterator() {
            assertThat(queue.isFull())
                    .as("The queue should be full")
                    .isTrue();

            var iterator = queue.iterator();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(iterator)
                    .as("The iterator should not be null").isNotNull();
            assertThat(buffer)
                    .as("The buffer should not be null").isNotNull();

            int numElements = 0;
            while (iterator.hasNext()) {
                iterator.next();
                numElements++;
            }
            assertThat(numElements)
                    .as("The iterator should iterate through the same number of elements as the buffer's size.")
                    .isEqualTo(buffer.length);
        }

        @Test
        @DisplayName("when calling the method put, a FullBoundedQueueException is thrown.")
        void putFails() {
            assertThat(queue.isFull())
                    .as("The queue should be full")
                    .isTrue();
            assertThatExceptionOfType(FullBoundedQueueException.class)
                    .isThrownBy(() -> queue.put(5))
                    .withMessage("put: full bounded queue");
        }
    }

    /**
     * An empty queue has already been tested {@link emptyQueue here}.<br/>
     * A full queue has already been tested {@link fullQueue here}.
     */
    @Nested
    @DisplayName("Given a queue, when the buffer is checked, all the elements are null except for the values in the queue:")
    class hasAllNullElementsInTheArrayExceptTheOnesPresentInTheQueue {
        @Test
        @DisplayName("1. [- - -] () | Empty")
        void empty() {
            assertThat(queue.isEmpty())
                    .as("The queue should be empty")
                    .isTrue();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).as("The buffer should not be null").isNotNull()
                    .as("All elements of the buffer should be null").containsOnlyNulls();
        }

        @Test
        @DisplayName("2. [- 3 -] (3) | One element")
        void oneElement() {
            // Setup
            queue.put(1);
            queue.put(3);
            queue.get();

            check();
        }

        @Test
        @DisplayName("3. [5 3 -] (5 -> 3) | Two elements together")
        void twoElementsTogether() {
            // Setup
            queue.put(5);
            queue.put(3);

            check();
        }

        @Test
        @DisplayName("4. [4 - 9] (9 -> 4) | Two elements looping")
        void twoElementsLooping() {
            // Setup
            queue.put(1);
            queue.put(2);
            queue.put(9);
            queue.get();
            queue.get();
            queue.put(4);

            check();
        }

        @Test
        @DisplayName("5. [4 7 9] (4 -> 7 -> 9) | Full")
        void full() {
            // Setup
            queue.put(4);
            queue.put(7);
            queue.put(9);

            assertThat(queue.isFull())
                    .as("The queue should be full")
                    .isTrue();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).as("The buffer should not be null").isNotNull()
                    .as("All elements of the buffer should not be null").doesNotContainNull();
        }

        private void check() {
            assertThat(queue.isEmpty())
                    .as("The queue should not be empty")
                    .isFalse();
            assertThat(queue.isFull())
                    .as("The queue should not be full")
                    .isFalse();

            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).isNotNull().as("The buffer should not be null");

            Integer first = (Integer) ReflectionTestUtils.getField(queue, "first");
            assertThat(first).as("The first attribute should not be null").isNotNull();
            Integer nextFree = (Integer) ReflectionTestUtils.getField(queue, "nextFree");
            assertThat(nextFree).as("The nextFree attribute should not be null").isNotNull();

            int j = first;
            boolean finished = false;
            for (int i = 0; i < buffer.length; i++) {
                if (j == nextFree) finished = true;
                if (finished) assertThat(buffer[j])
                        .as("The out-of-bounds element should be null")
                        .isNull();
                else assertThat(buffer[j])
                        .as("The in-bounds element should not be null")
                        .isNotNull();

                j = ReflectionTestUtils.invokeMethod(queue, "advance", j);
            }
        }
    }

    @Nested
    @DisplayName("Tests for loops:")
    class Loops {
        @Test
        @DisplayName("Given a non-empty nor-full queue with an element at the end and beginning of the buffer, " +
                "when getting a value, the *first* attribute becomes 0.")
        void firstAttributeLoopsAround() {
            // Setup
            queue.put(4);
            queue.put(7);
            queue.put(9);
            queue.get();
            queue.get();
            queue.put(1);

            // Given
            assertThat(ReflectionTestUtils.getField(queue, "first"))
                    .as("The *first* position should be on index 2")
                    .isEqualTo(2);

            // When
            queue.get();

            // Then
            assertThat(ReflectionTestUtils.getField(queue, "first"))
                    .as("The *first* position should be on index 0")
                    .isEqualTo(0);

        }
    }

}