package org.mps.boundedqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * <h1>Tests</h1>
 * <ul>
 *     <li>
 *         <b>Tests for invalid arguments or operations:</b>
 *         <ul>
 *             <li>
 *                 Given a non-positive size when creating a queue,
 *                 an {@link IllegalArgumentException} will be thrown.
 *             </li>
 *             <li>
 *                 Given a not-full queue, when putting a null element,
 *                 an {@link IllegalArgumentException} will be thrown.
 *             </li>
 *             <li>
 *                 Given the queue [{4} 7 -] (4 -> 7), when iterating the queue out of the queue's bounds,
 *                 an {@link NoSuchElementException} is thrown.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>Given a non-empty queue, when the queue is iterated, only not-null elements are iterated through.</li>
 *     <li>
 *         <b>Given an empty queue:</b>
 *         <ul>
 *             <li>when the queue is iterated, there's no elements to iterate through.</li>
 *             <li>when calling the method {@code get()}, an {@link EmptyBoundedQueueException} is thrown.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>Given an non-empty, non-full queue:</b>
 *         <ul>
 *             <li>
 *                 when the queue is iterated, the number of elements to iterate through is less than
 *                 the buffer's size (but equal to the queue's size).
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>Given a full queue:</b>
 *         <ul>
 *             <li>
 *                 when the queue is iterated, the number of elements to iterate through equals
 *                 the buffer's size (and the queue size).
 *             </li>
 *             <li>when calling the method put, a {@link FullBoundedQueueException} is thrown.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         Given a queue, when the buffer is checked, all the elements are {@code null} except for
 *         the values in the queue:
 *         <ol>
 *             <li>[- - -] () | Empty</li>
 *             <li>[- {3} -] (3) | One element</li>
 *             <li>[{5} 3 -] (5 -> 3) | Two elements together</li>
 *             <li>[4 - {9}] (9 -> 4) | Two elements looping</li>
 *             <li>[{4} 7 9] (4 -> 7 -> 9) | Full</li>
 *         </ol>
 *     </li>
 *     <li>
 *         <b>Explicit tests for changes in indexes with or without loops:</b>
 *         <ul>
 *             <li>
 *                 Without loops:
 *                 <ul>
 *                     <li>
 *                         Given the queue [{4} 7 -] (4 -> 7), when getting a value,
 *                         the <i>first</i> attribute becomes {@code 1}.
 *                     </li>
 *                     <li>
 *                         Given the queue [{4} - -] (4), when putting a valid value,
 *                         the <i>nextFree</i> attribute becomes {@code 2}.
 *                     </li>
 *                     <li>
 *                         Given the queue [{4} 7 -] (4 -> 7), when starting to iterate the queue,
 *                         the index of the iterator becomes {@code 1}.
 *                     </li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 With loops:
 *                 <ul>
 *                     <li>
 *                         Given the queue [1 - {9}] (9 -> 1), when getting a value,
 *                         the <i>first</i> attribute loops to {@code 0}.
 *                     </li>
 *                     <li>
 *                         Given the queue [{4} 7 -] (4 -> 7), when putting a valid value,
 *                         the <i>nextFree</i> attribute loops to {@code 0}.
 *                     </li>
 *                     <li>
 *                         Given the queue [4 7 {9}] (9 -> 4 -> 7), when starting to iterate the queue,
 *                         the index of the iterator loops to {@code 0}.
 *                     </li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 */
class ArrayBoundedQueueTest {

    private ArrayBoundedQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayBoundedQueue<>(3);
    }

    @Nested
    @DisplayName("Tests for invalid arguments or operations:")
    class InvalidArguments {
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

        @Test
        @DisplayName("Given the queue [{4} 7 -] (4 -> 7), " +
                "when iterating the queue out of the queue's bounds, an NoSuchElementException is thrown.")
        void iteratorIteratesOutOfBounds() {
            // Setup
            queue.put(4);
            queue.put(7);
            var iterator = queue.iterator();
            iterator.next();
            iterator.next();

            // Given
            assertThat(ReflectionTestUtils.getField(iterator, "current"))
                    .as("The index of the iterator should be 2")
                    .isEqualTo(2);

            // When - Then
            assertThatExceptionOfType(NoSuchElementException.class)
                    .isThrownBy(iterator::next)
                    .withMessage("next: bounded queue iterator exhausted");
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
        @DisplayName("when the queue is iterated, " +
                "the number of elements to iterate through is less than the buffer's size (but equal to the queue's size).")
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
                    .isLessThan(buffer.length)
                    .as("The iterator should iterate through the same number of elements as the queue's current size.")
                    .isEqualTo(queue.size());
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
        @DisplayName("when the queue is iterated, " +
                "the number of elements to iterate through equals the buffer's size (and the queue size).")
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
                    .isEqualTo(buffer.length)
                    .as("The iterator should iterate through the same number of elements as the queue's current size.")
                    .isEqualTo(queue.size());
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
        @DisplayName("2. [- {3} -] (3) | One element")
        void oneElement() {
            // Setup
            queue.put(1);
            queue.put(3);
            queue.get();

            check();
        }

        @Test
        @DisplayName("3. [{5} 3 -] (5 -> 3) | Two elements together")
        void twoElementsTogether() {
            // Setup
            queue.put(5);
            queue.put(3);

            check();
        }

        @Test
        @DisplayName("4. [4 - {9}] (9 -> 4) | Two elements looping")
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
        @DisplayName("5. [{4} 7 9] (4 -> 7 -> 9) | Full")
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
    @DisplayName("Explicit tests for changes in indexes with or without loops:")
    class Loops {
        @Test
        @DisplayName("Given the queue [{4} 7 -] (4 -> 7), " +
                "when getting a value, the *first* attribute becomes 1.")
        void firstAttributeWithoutLoop() {
            // Setup
            queue.put(4);
            queue.put(7);

            // Given
            assertThat(ReflectionTestUtils.getField(queue, "first"))
                    .as("The *first* position should be on index 0")
                    .isEqualTo(0);

            // When
            queue.get();

            // Then
            assertThat(ReflectionTestUtils.getField(queue, "first"))
                    .as("The *first* position should be on index 1")
                    .isEqualTo(1);

        }

        @Test
        @DisplayName("Given the queue [1 - {9}] (9 -> 1), " +
                "when getting a value, the *first* attribute loops to 0.")
        void firstAttributeWithLoop() {
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

        @Test
        @DisplayName("Given the queue [{4} - -] (4), " +
                "when putting a valid value, the *nextFree* attribute becomes 2.")
        void nextFreeAttributeWithoutLoop() {
            // Setup
            queue.put(4);

            // Given
            assertThat(ReflectionTestUtils.getField(queue, "nextFree"))
                    .as("The *nextFree* position should be on index 1")
                    .isEqualTo(1);

            // When
            queue.put(7);

            // Then
            assertThat(ReflectionTestUtils.getField(queue, "nextFree"))
                    .as("The *nextFree* position should be on index 2")
                    .isEqualTo(2);
        }

        @Test
        @DisplayName("Given the queue [{4} 7 -] (4 -> 7), " +
                "when putting a valid value, the *nextFree* attribute loops to 0.")
        void nextFreeAttributeWithLoop() {
            // Setup
            queue.put(4);
            queue.put(7);

            // Given
            assertThat(ReflectionTestUtils.getField(queue, "nextFree"))
                    .as("The *nextFree* position should be on index 2")
                    .isEqualTo(2);

            // When
            queue.put(9);

            // Then
            assertThat(ReflectionTestUtils.getField(queue, "nextFree"))
                    .as("The *nextFree* position should be on index 0")
                    .isEqualTo(0);
        }

        @Test
        @DisplayName("Given the queue [{4} 7 -] (4 -> 7), " +
                "when starting to iterate the queue, the index of the iterator becomes 1.")
        void iteratorIteratesWithoutLoop() {
            // Setup
            queue.put(4);
            queue.put(7);
            var iterator = queue.iterator();

            // Given
            assertThat(ReflectionTestUtils.getField(iterator, "current"))
                    .as("The index of the iterator should be 0")
                    .isEqualTo(0);

            // When
            iterator.next();

            // Then
            assertThat(ReflectionTestUtils.getField(iterator, "current"))
                    .as("The index of the iterator should be 1")
                    .isEqualTo(1);
        }

        @Test
        @DisplayName("Given the queue [4 7 {9}] (9 -> 4 -> 7), " +
                "when starting to iterate the queue, the index of the iterator loops to 0.")
        void iteratorIteratesWithLoop() {
            // Setup
            ReflectionTestUtils.setField(queue, "first", 2);
            ReflectionTestUtils.setField(queue, "nextFree", 2);
            queue.put(9);
            queue.put(4);
            queue.put(7);
            var iterator = queue.iterator();

            // Given
            assertThat(ReflectionTestUtils.getField(iterator, "current"))
                    .as("The index of the iterator should be 2")
                    .isEqualTo(2);

            // When
            iterator.next();

            // Then
            assertThat(ReflectionTestUtils.getField(iterator, "current"))
                    .as("The index of the iterator should be 0")
                    .isEqualTo(0);
        }
    }
}