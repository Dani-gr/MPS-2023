package org.mps.boundedqueue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO create comment
 */
class ArrayBoundedQueueTest {

    private ArrayBoundedQueue<Integer> queue;

    @Nested
    class emptyQueueHasAllNullElements {
        @Test
        void inTheArray() {
            queue = new ArrayBoundedQueue<>(3);
            assertThat(queue.isEmpty()).isTrue()
                    .as("La cola deber�a crearse vac�a");
            Object[] buffer = (Object[]) ReflectionTestUtils.getField(queue, "buffer");
            assertThat(buffer).isNotNull()
                    .as("El buffer no deber�a ser nulo");
            assertThat(buffer).allSatisfy(element -> assertThat(element).isNull())
                    .as("Todos los elementos del buffer deber�an ser nulos al crearse la cola");
        }

        @Test
        void inTheIterator() {
            queue = new ArrayBoundedQueue<>(3);
            assertThat(queue.isEmpty()).isTrue();
            var iterator = queue.iterator();
            assertThat(iterator).isNotNull();
            iterator.forEachRemaining(element -> assertThat(element).isNull());
            //TODO
        }
    }
}