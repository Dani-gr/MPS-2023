package org.mps.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CredentialStoreSetTest {

    @Test
    public void credentialExistsReturnsFalseIfTheCredentialStoreIsEmpty() {
        Email dummyEmail = new Email("mps@uma.es");
        PasswordString dummyPasswordString = new PasswordString("fsjfh.6kdksd");
        assertFalse(new CredentialStoreSet().credentialExists(dummyEmail, dummyPasswordString));
    }

    @Test
    public void storeAddsANewCredentialIfTheStoreIsEmpty() {
        Email email = new Email("mps@uma.es");
        PasswordString passwordString = new PasswordString("fsjfh.6kdksd");

        CredentialStore credentialStore = new CredentialStoreSet();
        credentialStore.store(email, passwordString);

        assertTrue(credentialStore.credentialExists(email, passwordString));
        assertEquals(1, credentialStore.size());
    }

    @Test
    public void storeThrowsAnExceptionIfTheCredentialExists() {
        Email email = new Email("mps@uma.es");
        PasswordString passwordString = new PasswordString("fsjfh.6kdksd");

        CredentialStore credentialStore = new CredentialStoreSet();
        credentialStore.store(email, passwordString);

        assertThrows(CredentialExistsException.class, () -> credentialStore.store(email, passwordString));
    }

    @Test
    public void sizeReturnsThreeIfTheCredentialStoreHasThreeCredentials() {
        CredentialStore credentialStore = new CredentialStoreSet();
        credentialStore.store(new Email("mps@uma.es"), new PasswordString("asdfwf.,03234"));
        credentialStore.store(new Email("sii@uma.es"), new PasswordString("0ghue,743"));
        credentialStore.store(new Email("mps@ugr.es"), new PasswordString("p.hfa81djd"));

        assertEquals(3, credentialStore.size());
    }
}
