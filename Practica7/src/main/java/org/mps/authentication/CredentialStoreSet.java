package org.mps.authentication;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CredentialStoreSet implements CredentialStore {

    private static class CredentialPair {

        private final Email email;
        private final PasswordString passwordString;

        public CredentialPair(Email email, PasswordString passwordString) {
            this.email = email;
            this.passwordString = passwordString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CredentialPair pair = (CredentialPair) o;

            if (!Objects.equals(email, pair.email)) return false;
            return Objects.equals(passwordString, pair.passwordString);
        }

        @Override
        public int hashCode() {
            int result = email != null ? email.hashCode() : 0;
            result = 31 * result + (passwordString != null ? passwordString.hashCode() : 0);
            return result;
        }
    }

    private final Set<CredentialPair> credentials = new HashSet<>();

    @Override
    public boolean credentialExists(Email email, PasswordString passwordString) {
        return credentials.contains(new CredentialPair(email, passwordString));
    }

    @Override
    public void store(Email email, PasswordString passwordString) throws CredentialExistsException {
        CredentialPair newCredentials = new CredentialPair(email, passwordString);
        if (credentials.contains(newCredentials)) throw new CredentialExistsException();
        else credentials.add(newCredentials);
    }

    @Override
    public int size() {
        return credentials.size();
    }
}
