import es.uma.informatica.mps.Calendario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalendarioTest {

    @Nested
    @DisplayName("Al llamar al método diaSemana() de la clase Calendario")
    class metodoDiaSemana {
        @Nested
        @DisplayName("con condición: \"Mes entre 1 y 12\"")
        class MesEntreUnoYDoce {
            @Test
            @Tag("1")
            @DisplayName("se obtiene un resultado válido dado un mes entre 1 y 12 (inclusive).")
            void mesValido() {
                assertDoesNotThrow(() -> Calendario.diaSemana(25, 11, 2022));
            }

            @Tags({@Tag("2"), @Tag("3")})
            @ParameterizedTest
            @ValueSource(ints = {-1, 15})
            @DisplayName("se obtiene un resultado inválido dado un mes anterior a 1 o posterior a 12.")
            void mesInvalido(int mes) {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(25, mes, 2022));
            }
        }

        @Nested
        @DisplayName("con condición: \"Día entre 1 y el máximo del mes correspondiente\"")
        class DiaEntreUnoYElMaximoDelMes {

            @Tags({@Tag("5"), @Tag("8"), @Tag("11"), @Tag("14"), @Tag("17")})
            @ParameterizedTest
            @ValueSource(ints = {0, 1, 2, 3, 4})
            @DisplayName("se obtiene un resultado inválido dado un día menor que 1.")
            void diaMenorInvalido(int index) {
                int[] data = {1, 1900, 4, 2002, 2, 503, 2, 1500, 2, 2000};
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(-1, data[2 * index], data[2 * index + 1]));
            }

            @Nested
            @DisplayName("cuando se da un mes de 31 días (enero, marzo, mayo, julio, agosto, octubre o diciembre)")
            class MesDeTreintaYUnDias {
                @Test
                @Tag("4")
                @DisplayName("se obtiene un resultado válido dado un día entre 1 y 31.")
                void diaValido() {
                    assertDoesNotThrow(() -> Calendario.diaSemana(12, 10, 2032));
                }

                @Test
                @Tag("6")
                @DisplayName("se obtiene un resultado inválido dado un día mayor que 31.")
                void diaMayorInvalido() {
                    assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(40, 5, 2023));
                }
            }

            @Nested
            @DisplayName("cuando se da un mes de 30 días (abril, junio, septiembre o noviembre)")
            class MesDeTreintaDias {
                @Test
                @Tag("7")
                @DisplayName("se obtiene un resultado válido dado un día entre 1 y 30")
                void diaValido() {
                    assertDoesNotThrow(() -> Calendario.diaSemana(1, 4, 1998));
                }

                @Test
                @Tag("9")
                @DisplayName("se obtiene un resultado inválido dado un día mayor que 30")
                void diaMayorInvalido() {
                    assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(31, 6, 500));
                }
            }

            @Nested
            @DisplayName("cuando se da el mes de febrero")
            class Febrero {
                @Nested
                @DisplayName("y un año no bisiesto")
                class NoBisiesto {
                    @Test
                    @Tag("10")
                    @DisplayName("se obtiene un resultado válido dado un día entre 1 y 28.")
                    void diaValido() {
                        assertDoesNotThrow(() -> Calendario.diaSemana(1, 2, 2023));
                    }

                    @Test
                    @Tag("12")
                    @DisplayName("se obtiene un resultado inválido dado un día mayor que 28.")
                    void diaMayorInvalido() {
                        assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(29, 2, 2100));
                    }
                }

                @Nested
                @DisplayName("y un año bisiesto")
                class Bisiesto {
                    @Tags({@Tag("13"), @Tag("16")})
                    @ParameterizedTest
                    @ValueSource(ints = {1000, 2000})
                    @DisplayName("se obtiene un resultado válido dado un día entre 1 y 29.")
                    void diaValido(int anio) {
                        assertDoesNotThrow(() -> Calendario.diaSemana(1, 2, anio));
                    }

                    @Tags({@Tag("15"), @Tag("18")})
                    @ParameterizedTest
                    @ValueSource(ints = {1000, 2000})
                    @DisplayName("se obtiene un resultado inválido dado un día mayor que 29.")
                    void diaMayorInvalido(int anio) {
                        assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(30, 2, anio));
                    }
                }
            }
        }

        @Nested
        @DisplayName("con condición: \"Fecha posterior al 1 de marzo del año 4 d.C\"")
        class FechaPosteriorAlComienzoDelCalendarioJuliano {
            @Tag("19")
            @Test
            @DisplayName("se obtiene un resultado válido dada una fecha posterior al 01/03/0004 d.C.")
            void fechaValida() {
                assertDoesNotThrow(() -> Calendario.diaSemana(25, 5, 7));
            }

            @Tag("20")
            @Test
            @DisplayName("se obtiene un resultado inválido dada una fecha anterior al 01/03/0004 d.C.")
            void fechaInvalida() {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(23, 2, 4));
            }
        }

        @Nested
        @DisplayName("con condición: \"Los días 5 a 14 de octubre de 1582 no existieron\"")
        class FechasDeTransicionAlCalendarioGregoriano {
            @Tags({@Tag("21"), @Tag("22")})
            @ParameterizedTest
            @ValueSource(ints = {1, 17})
            @DisplayName("se obtiene un resultado válido dada una fecha anterior al 05/10/1582 o posterior al 14/10/1582.")
            void fechaValida(int dia) {
                assertDoesNotThrow(() -> Calendario.diaSemana(dia, 10, 1582));
            }

            @Tag("23")
            @Test
            @DisplayName("se obtiene un resultado inválido dada una fecha entre el 05/10/1582 y el 14/10/1582 (inclusive).")
            void fechaInvalida() {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(10, 10, 1582));
            }
        }
    }
}
