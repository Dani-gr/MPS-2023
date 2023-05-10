package org.mps.authentication;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationTest {
    @Nested
    @Tag("Fase1")
    @DisplayName("Given only the UserRegistration class, when calling the method register()")
    class RegisterFase1 {
        UserRegistration userRegistration;
        CredentialValidator validator;
        CredentialStore store;

        @BeforeEach
        void setUp() {
            validator = Mockito.mock(CredentialValidator.class);
            store = Mockito.mock(CredentialStore.class);
            userRegistration = new UserRegistration(validator, store);
        }


    }

    @Nested
    @Tag("Fase2")
    @DisplayName("Given the implementation of classes UserRegistration and CredentialValidatorImpl, when calling the method register()")
    class RegisterFase2 {
        UserRegistration userRegistration;
        CredentialValidator validator;
        CredentialStore store;

        @BeforeEach
        void setUp() {
            store = Mockito.mock(CredentialStore.class);
            validator = new CredentialValidatorImpl(store);
            userRegistration = new UserRegistration(validator, store);
        }

        /**
         * It doesn't work, since the implementation of {@link CredentialValidatorImpl} is erroneous.
         */
        @Test
        @Tag("Fase2")
        @DisplayName("with valid email and password, then the credentials are stored.")
        void validDataRegistersInSet() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(true);

            userRegistration.register(email, password);

            Mockito.verify(store, Mockito.times(1)).store(email, password);
        }

        @Test
        @Tag("Fase2")
        @DisplayName("with invalid email, then the credentials are not stored.")
        void invalidEmailIsNotRegistered() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(false);
            Mockito.when(password.validate()).thenReturn(true);

            userRegistration.register(email, password);

            Mockito.verify(store, Mockito.never()).store(email, password);
        }

        @Test
        @Tag("Fase2")
        @DisplayName("with invalid password, then the credentials are not stored.")
        void invalidPasswordIsNotRegistered() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(false);

            userRegistration.register(email, password);

            Mockito.verify(store, Mockito.never()).store(email, password);
        }

        @Test
        @Tag("Fase2")
        @DisplayName("with valid email and password, but already registered, then the credentials are not stored again.")
        void alreadyRegisteredDataFails() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(true);
            userRegistration.register(email, password);
            Mockito.when(store.credentialExists(email, password)).thenReturn(true);

            userRegistration.register(email, password);

            assertTrue(store.credentialExists(email, password));
            Mockito.verify(store, Mockito.times(1)).store(email, password);
        }
    }

    @Nested
    @Tag("Fase3")
    @DisplayName("Given the implementation of every class except Email and PasswordString, when calling the method register()")
    class RegisterFase3 {
        UserRegistration userRegistration;
        CredentialValidator validator;
        CredentialStore store;

        @BeforeEach
        void setUp() {
            store = new CredentialStoreSet();
            validator = new CredentialValidatorImpl(store);
            userRegistration = new UserRegistration(validator, store);
        }

        /**
         * It doesn't work, since the implementation of {@link CredentialValidatorImpl} is erroneous.
         */
        @Test
        @Tag("Fase3")
        @DisplayName("with valid email and password, then the credentials are stored.")
        void validDataRegistersInSet() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(true);

            userRegistration.register(email, password);

            assertTrue(store.credentialExists(email, password));
        }

        @Test
        @Tag("Fase3")
        @DisplayName("with invalid email, then the credentials are not stored.")
        void invalidEmailIsNotRegistered() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(false);
            Mockito.when(password.validate()).thenReturn(true);

            userRegistration.register(email, password);

            assertFalse(store.credentialExists(email, password));
        }

        @Test
        @Tag("Fase3")
        @DisplayName("with invalid password, then the credentials are not stored.")
        void invalidPasswordIsNotRegistered() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(false);

            userRegistration.register(email, password);

            assertFalse(store.credentialExists(email, password));
        }

        /**
         * It doesn't work, since the implementation of {@link CredentialValidatorImpl} is erroneous.
         */
        @Test
        @Tag("Fase3")
        @DisplayName("with valid email and password, but already registered, then the credentials are not stored again.")
        void alreadyRegisteredDataFails() {
            Email email = Mockito.mock(Email.class);
            PasswordString password = Mockito.mock(PasswordString.class);
            Mockito.when(email.validate()).thenReturn(true);
            Mockito.when(password.validate()).thenReturn(true);
            userRegistration.register(email, password);
            int sizeBefore = store.size();

            userRegistration.register(email, password);

            assertTrue(store.credentialExists(email, password));
            assertEquals(sizeBefore, store.size());
        }
    }

    @Nested
    @Tag("Fase4")
    @DisplayName("Given the implementation of every class, when calling the method register()")
    class RegisterFase4 {
        UserRegistration userRegistration;
        CredentialValidator validator;
        CredentialStore store;

        @BeforeEach
        void setUp() {
            store = new CredentialStoreSet();
            validator = new CredentialValidatorImpl(store);
            userRegistration = new UserRegistration(validator, store);
        }

        /**
         * It doesn't work, since the implementation of {@link CredentialValidatorImpl} is erroneous.
         */
        @Test
        @Tag("Fase4")
        @DisplayName("with valid email and password, then the credentials are stored.")
        void validDataRegistersInSet() {
            Email email = new Email("correo@correcto.es");
            PasswordString password = new PasswordString("abcd.efg1");

            userRegistration.register(email, password);

            assertTrue(store.credentialExists(email, password));
        }

        @Test
        @Tag("Fase4")
        @DisplayName("with invalid email, then the credentials are not stored.")
        void invalidEmailIsNotRegistered() {
            Email email = new Email("correo@@incorrecto.es");
            PasswordString password = new PasswordString("abcd.efg1");

            userRegistration.register(email, password);

            assertFalse(store.credentialExists(email, password));
        }

        @Test
        @Tag("Fase4")
        @DisplayName("with invalid password, then the credentials are not stored.")
        void invalidPasswordIsNotRegistered() {
            Email email = new Email("correo@correcto.es");
            PasswordString password = new PasswordString("corto");

            userRegistration.register(email, password);

            assertFalse(store.credentialExists(email, password));
        }

        /**
         * It doesn't work, since the implementation of {@link CredentialValidatorImpl} is erroneous.
         */
        @Test
        @Tag("Fase4")
        @DisplayName("with valid email and password, but already registered, then the credentials are not stored again.")
        void alreadyRegisteredDataFails() {
            Email email = new Email("correo@correcto.es");
            PasswordString password = new PasswordString("abcd.efg1");
            userRegistration.register(email, password);
            int sizeBefore = store.size();

            userRegistration.register(email, password);

            assertTrue(store.credentialExists(email, password));
            assertEquals(sizeBefore, store.size());
        }
    }
}