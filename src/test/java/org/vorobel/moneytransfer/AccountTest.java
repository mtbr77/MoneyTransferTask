package org.vorobel.moneytransfer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.vorobel.moneytransfer.model.Account;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void testCreation() {
        var initialBalance = "0.01";
        Account account = new Account(initialBalance);
        assertEquals(initialBalance, account.balance);
        assertDoesNotThrow(() -> new Account());
        assertDoesNotThrow(() -> new Account(initialBalance));
    }
}