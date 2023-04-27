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
        assertThat(queue.isFull()).isFalse().as("The queue should not be full");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> queue.put(null))
                .withMessage("put: element cannot be null");
    }

    @Nested
    @DisplayName("Given an empty queue")
    class emptyQueue {
        @Test
        @DisplayName("when the buffer is checked, all the elements are null.")
        void hasAllNullElementsInTheArray() {
            assertThat(queue.isEmpty())
                    .as("The queue should be created empty")
                    .isTrue();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).isNotNull().as("The buffer should not be null")
                    .containsOnlyNulls().as("All elements of the buffer should be null");
        }

        @Test
        @DisplayName("when the queue is iterated, there's no elements to iterate through.")
        void hasNoElementsInTheIterator() {
            assertThat(queue.isEmpty()).isTrue()
                    .as("The queue should be created empty");
            var iterator = queue.iterator();
            assertThat(iterator).isNotNull()
                    .as("The iterator should not be null");
            assertThat(iterator.hasNext()).isFalse()
                    .as("The iterator should not have any elements to iterate from.");
        }

        @Test
        @DisplayName("when calling the method get, an EmptyBoundedQueueException is thrown.")
        void getFails() {
            assertThat(queue.isEmpty()).isTrue()
                    .as("The queue should be created empty");
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
        @DisplayName("when the buffer is checked, all the elements are null except por 1.")
        void hasAllNullElementsButOneInTheArray() {
            assertThat(queue.isEmpty()).isFalse()
                    .as("The queue should not be empty");
            assertThat(queue.isFull()).isFalse()
                    .as("The queue should not be full");

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

        @Test
        @DisplayName("when the queue is iterated, only not-null elements are iterated through.")
        void onlyOneNonNullElementWhenIterating() {
            assertThat(queue.isEmpty()).isFalse()
                    .as("La cola no debería ser vacía");
            var iterator = queue.iterator();
            assertThat(iterator).isNotNull()
                    .as("El iterador no debería ser nulo");
            iterator.forEachRemaining(element -> assertThat(element)
                    .as("Todos los elementos del iterador deberían ser no-nulos")
                    .isNotNull());
        }
    }
}