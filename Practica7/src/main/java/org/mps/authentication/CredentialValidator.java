package org.mps.authentication;

public interface CredentialValidator {
    enum ValidationStatus {EMAIL_INVALID, PASSWORD_INVALID, EXISTING_CREDENTIAL, VALIDATION_OK}

    ValidationStatus validate(Email email, PasswordString passwordString);
}
