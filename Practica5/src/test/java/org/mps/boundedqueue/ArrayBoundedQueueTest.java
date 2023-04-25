package org.mps.boundedqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * TODO create comment
 */
class ArrayBoundedQueueTest {

    private ArrayBoundedQueue<Integer> queue;

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    @DisplayName("Given a non-positive size when creating a queue, an IllegalArgumentException will be thrown.")
    void nonPositiveSizeThrowsException(){
        assertThatThrownBy(() -> queue = new ArrayBoundedQueue<>(0),
                "A non-positive size must be provided", IllegalArgumentException.class)
                .hasMessage("ArrayBoundedException: capacity must be positive");
    }

    @Nested
    @DisplayName("Given an empty queue")
    class emptyQueueHasAllNullElements {
        @Test
        @DisplayName("when the buffer is checked, all the elements are null.")
        void inTheArray() {
            queue = new ArrayBoundedQueue<>(3);
            assertThat(queue)
                    .as("The queue should be created empty")
                    .isEmpty();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).isNotNull()
                    .as("The buffer should not be null")
                    .allSatisfy(element -> assertThat(element).isNull())
                    .as("All elements of the buffer should be null");
        }

        @Test
        @DisplayName("when the queue is iterated, there's no elements to iterate through.")
        void inTheIterator() {
            queue = new ArrayBoundedQueue<>(3);
            assertThat(queue.isEmpty()).isTrue()
                    .as("La cola debería crearse vacía");
            var iterator = queue.iterator();
            assertThat(iterator).isNotNull()
                    .as("El iterador no debería ser nulo");
            assertThat(iterator.hasNext()).isFalse()
                    .as("El iterador no debería tener elementos sobre los que iterar al crearse la cola");
        }
    }

    @Nested
    @DisplayName("Given an non-empty queue")
    class nonEmptyQueue {
        @BeforeEach
        void setUp(){
            queue = new ArrayBoundedQueue<>(3);
        }
        @Test
        @DisplayName("when the buffer is checked, all the elements are null.")
        void inTheArray() {
            assertThat(queue)
                    .as("The queue should be created empty")
                    .isEmpty();
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).isNotNull()
                    .as("The buffer should not be null")
                    .allSatisfy(element -> assertThat(element).isNull())
                    .as("All elements of the buffer should be null");
        }

        @Test
        @DisplayName("when the queue is iterated, all elements to iterate through are not null.")
        void noNullElementsWhenIterating() {
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