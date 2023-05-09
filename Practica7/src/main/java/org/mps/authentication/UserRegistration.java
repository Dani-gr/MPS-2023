package org.mps.authentication;

import org.mps.authentication.CredentialValidator.ValidationStatus;

public class UserRegistration {
    private final CredentialValidator validator;
    private final CredentialStore store;

    public UserRegistration(CredentialValidator validator, CredentialStore credentialStore) {
        this.validator = validator;
        this.store = credentialStore;
    }

    public void register(Email email, PasswordString passwordString) {
        ValidationStatus status = validator.validate(email, passwordString);

        if (status == ValidationStatus.VALIDATION_OK) store.store(email, passwordString);
    }
}
