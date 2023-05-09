package org.mps.authentication;

import java.util.stream.Stream;

/**
 * Class used to store and validate an email address.
 * <p>
 * An email is valid if:
 * 1. It has exactly one "at" symbol (@) separating the mailbox and the domain
 * 2. The domain must be a sequence of subdomain names containing letters, numbers or dash, with a minimum
 * of two non-empty subdomain names and without a dash at the beginning or the end.
 * 3. The mailbox must be a non-empty sequence of characters composed of letters, numbers, dash,
 * underscore, or dot. Two dots cannot be adjacent.
 */
public class Email {
    private final String email;

    public Email(String email) {
        this.email = email;
    }

    public boolean validate() {

        if (email.indexOf('@') < 0) {
            // No @
            return false;
        }

        String[] mailboxDomain = email.split("@");

        if (mailboxDomain.length > 2) {
            // More than one @
            return false;
        }

        return checkMailbox(mailboxDomain[0]) && checkDomain(mailboxDomain[1]);
    }

    private boolean checkDomain(String domain) {
        String[] subdomains = domain.split("\\.");
        if (subdomains.length < 2) return false;
        return Stream.of(subdomains).allMatch(this::checkSubdomain);
    }

    private boolean checkSubdomain(String subdomain) {
        return subdomain.length() > 0 &&
                subdomain.chars().allMatch(ch -> Character.isLetter(ch) || Character.isDigit(ch) || ch == '-') &&
                !subdomain.startsWith("-") &&
                !subdomain.endsWith("-");
    }

    private boolean checkMailbox(String mailbox) {
        if (mailbox.isEmpty()) return false;

        if (!mailbox.chars().allMatch(ch -> Character.isDigit(ch) ||
                Character.isLetter(ch) ||
                "-_.".indexOf(ch) >= 0)) {
            return false;
        }

        return !mailbox.contains("..");
    }
}
