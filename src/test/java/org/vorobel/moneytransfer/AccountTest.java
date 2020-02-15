package org.vorobel.moneytransfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {
    @Test
    void testCreation() {
        String initialBalance = "0.01";
        Account account = new Account(initialBalance);
        assertEquals(initialBalance, account.getBalance());
        assertDoesNotThrow(() -> new Account());
        assertDoesNotThrow(() -> new Account(initialBalance));
    }
}