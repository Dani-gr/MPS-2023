package collapseLines;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h2>Tests realizados</h2>
 * <ol>
 *     <li> Se lanza IndexOutOfBoundsException al comienzo.<br>
 *          Secuencia: 0 - 1 - 2</li>
 *     <li> En la primera iteración, ch == "\n" y last == "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 6 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch != "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 7 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch == "\n" y last == "\n"; <br>
 *          en la segunda iteración, ch != "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 6 - 8 - 4 - 5 - 7 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch == "\n" y last == "\n"; <br>
 *          en la segunda iteración, ch == "\n" y last == "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 6 - 8 - 4 - 5 - 6 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch != "\n"; <br>
 *          en la segunda iteración, ch != "\n"; <br>
 *          en la tercera iteración, ch == "\n" y last != "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 7 - 8 - 4 - 5 - 7 - 8 - 4 - 5 – 6 - 7 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch != "\n"; <br>
 *          en la segunda iteración, ch != "\n"; <br>
 *          en la tercera iteración, ch == "\n" y last != "\n"; <br>
 *          en la cuarta iteración, ch == "\n" y last == "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 7 - 8 - 4 - 5 - 7 - 8 - 4 - 5 – 6 - 7 - 8 - 4 - 5 - 6 - 8 - 4 - 9</li>
 *     <li> En la primera iteración, ch != "\n"; <br>
 *          en la segunda iteración, ch != "\n"; <br>
 *          en la tercera iteración, ch != "\n"; y termina.<br>
 *          Secuencia: 0 - 1 - 3 - 4 - 5 - 6 - 8 - 4 - 5 - 6 - 8 - 4 - 5 - 7 - 8 - 4 - 9</li>
 * </ol>
 */
class CollapseLinesTest {
    @BeforeAll
    @SuppressWarnings("InstantiationOfUtilityClass")
    static void setUp(){
        new CollapseLines(); //Para 100% de cobertura de línea
    }

    @Test
    @DisplayName("1. An empty String throws IndexOutOfBoundsException.")
    void emptyString() {
        assertThrows(IndexOutOfBoundsException.class, () -> CollapseLines.collapseNewLines(""));
    }

    @Test
    @DisplayName("2. A String of only a new line returns empty String.")
    void justOneNewLine() {
        String result = CollapseLines.collapseNewLines("\n");
        assertEquals("", result);
    }

    @Test
    @DisplayName("3. A String of only a non new line character returns it unchanged.")
    void justOneNormalChar() {
        String result = CollapseLines.collapseNewLines("R");
        assertEquals("R", result);
    }

    @Test
    @DisplayName("4. A String of only a non new line character after a new line returns the non new line character.")
    void oneNewLineThenOneNormalChar() {
        String result = CollapseLines.collapseNewLines("\ni");
        assertEquals("i", result);
    }

    @Test
    @DisplayName("5. A String of only a two new lines returns an empty String.")
    void twoNewLines() {
        String result = CollapseLines.collapseNewLines("\n\n");
        assertEquals("", result);
    }

    @Test
    @DisplayName("6. A String of two non new line characters before a new line returns the String unchanged.")
    void twoCharsThenOneNewLine() {
        String result = CollapseLines.collapseNewLines("ck\n");
        assertEquals("ck\n", result);
    }

    @Test
    @DisplayName("7. A String of two non new line characters before two " +
                    "new lines returns the String with one less new line.")
    void twoCharsThenTwoNewLines() {
        String result = CollapseLines.collapseNewLines("ro\n\n");
        assertEquals("ro\n", result);
    }

    @Test
    @DisplayName("8. A String of only three non new line characters returns it unchanged.")
    void testCollapseThreeNewLinesThenTwoNewLinesThenTwoNewLines() {
        String result = CollapseLines.collapseNewLines("lls");
        assertEquals("lls", result);
    }
}
