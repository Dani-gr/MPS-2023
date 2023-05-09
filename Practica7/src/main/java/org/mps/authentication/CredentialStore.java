package org.mps.authentication;

public interface CredentialStore {
    boolean credentialExists(Email email, PasswordString passwordString);

    void store(Email email, PasswordString passwordString) throws CredentialExistsException;

    int size();
}
