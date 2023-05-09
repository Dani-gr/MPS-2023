package org.mps.authentication;

/**
 * Class that validate that a birth data and a password are valid values
 */
public class CredentialValidatorImpl implements CredentialValidator {
    private final CredentialStore credentialStore;

    public CredentialValidatorImpl(CredentialStore credentialStore) {
        this.credentialStore = credentialStore;
    }

    @Override
    public ValidationStatus validate(Email email, PasswordString passwordString) {
        ValidationStatus result = ValidationStatus.VALIDATION_OK;
        if (!email.validate()) result = ValidationStatus.EMAIL_INVALID;
        else if (!passwordString.validate()) result = ValidationStatus.PASSWORD_INVALID;
        else if (!credentialStore.credentialExists(email, passwordString))
            result = ValidationStatus.EXISTING_CREDENTIAL;
        return result;
    }
}
