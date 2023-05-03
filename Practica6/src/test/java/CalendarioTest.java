import es.uma.informatica.mps.Calendario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalendarioTest {
    @Tags({@Tag("1"), @Tag("2"), @Tag("3")})
    @Nested
    @DisplayName("Condición: Mes entre 1 y 12")
    class MesEntreUnoYDoce {
        @Test
        @Tag("1")
        @DisplayName("Válido: Mes entre 1 y 12")
        void mesValido() {
            assertDoesNotThrow(() -> Calendario.diaSemana(25, 11, 2022));
        }

        @Tags({@Tag("2"), @Tag("3")})
        @ParameterizedTest
        @ValueSource(ints = {-1, 15})
        @DisplayName("Inválido: Mes anterior a 1 o posterior a 12")
        void mesInvalido(int mes) {
            assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(25, mes, 2022));
        }
    }

    @Nested
    @DisplayName("Condición: Día entre 1 y el máximo del mes correspondiente")
    class DiaEntreUnoYElMaximoDelMes {
        @Test
        @Tag("5")
        @DisplayName("Inválido: Día menor que 1")
        void diaMenorInvalido() {
            assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(-1, 7, 2006));
        }

        @Tags({@Tag("4"), @Tag("6")})
        @Nested
        @DisplayName("Mes de 31 días: Enero, marzo, mayo, julio, agosto, octubre o diciembre")
        class MesDeTreintaYUnDias {
            @Test
            @Tag("4")
            @DisplayName("Válido: Día entre 1 y 31")
            void diaValido() {
                assertDoesNotThrow(() -> Calendario.diaSemana(2, 5, 2023));
            }

            @Test
            @Tag("6")
            @DisplayName("Inválido: Día mayor que 31")
            void diaMayorInvalido() {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(40, 5, 2023));
            }
        }

        @Tags({@Tag("7"), @Tag("8")})
        @Nested
        @DisplayName("Mes de 30 días: Abril, junio, septiembre o noviembre")
        class MesDeTreintaDias {
            @Test
            @Tag("7")
            @DisplayName("Válido: Día entre 1 y 30")
            void diaValido() {
                assertDoesNotThrow(() -> Calendario.diaSemana(1, 4, 1998));
            }

            @Test
            @Tag("8")
            @DisplayName("Inválido: Día mayor que 30")
            void diaMayorInvalido() {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(31, 6, 500));
            }
        }

        @Tags({@Tag("9"), @Tag("10"), @Tag("11"), @Tag("12"), @Tag("13"), @Tag("14")})
        @Nested
        @DisplayName("Mes de 30 días: Abril, junio, septiembre o noviembre")
        class Febrero {
            @Test
            @Tag("7")
            @DisplayName("Válido: Día entre 1 y 30")
            void diaValido() {
                assertDoesNotThrow(() -> Calendario.diaSemana(1, 4, 1998));
            }

            @Test
            @Tag("8")
            @DisplayName("Inválido: Día mayor que 30")
            void diaMayorInvalido() {
                assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(31, 6, 500));
            }
        }
    }

    @Tags({@Tag("15"), @Tag("16")})
    @Nested
    @DisplayName("Condición: Fecha posterior al 1 de marzo del año 4 d.C")
    class FechaPosteriorAlComienzoDelCalendarioJuliano {
        @Tag("15")
        @Test
        @DisplayName("Válido: Fecha posterior al 01/03/0004 d.C")
        void fechaValida() {
            assertDoesNotThrow(() -> Calendario.diaSemana(25, 5, 4));
        }

        @Tag("16")
        @Test
        @DisplayName("Inválido: Fecha anterior al 01/03/0004 d.C")
        void fechaInvalida() {
            assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(23, 2, 4));
        }
    }

    @Tags({@Tag("17"), @Tag("18"), @Tag("19")})
    @Nested
    @DisplayName("Condición: Los días 5 a 14 de octubre de 1582 no existieron")
    class FechasDeTransicionAlCalendarioGregoriano {
        @Tags({@Tag("17"), @Tag("18")})
        @ParameterizedTest
        @ValueSource(ints = {1, 17})
        @DisplayName("Válido: Fecha anterior al 05/10/1582 o posterior al 14/10/1582")
        void fechaValida(int dia) {
            assertDoesNotThrow(() -> Calendario.diaSemana(dia, 10, 1582));
        }

        @Tag("19")
        @Test
        @DisplayName("Inválido: Fecha entre el 05/10/1582 y el 14/10/1582 (inclusive)")
        void fechaInvalida() {
            assertThrows(IllegalArgumentException.class, () -> Calendario.diaSemana(10, 10, 1582));
        }
    }
}
