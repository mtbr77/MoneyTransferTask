package org.vorobel.moneytransfer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void testCreation() {
        var initialBalance = "0.01";
        var account = new Account(initialBalance);
        assertEquals(initialBalance, account.getBalance());
        assertDoesNotThrow(() -> new Account());
        assertDoesNotThrow(() -> new Account(null, initialBalance));
    }
}