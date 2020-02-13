package org.vorobel.moneytransfer;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class NumbersTest {
    @Test
    void testDouble() {
        assertNotEquals("111111111111111.01", String.valueOf(111111111111111.01));
    }

    @Test
    void testBigDecimalWithDouble() {
        assertNotEquals("1.01", (new BigDecimal(1.01)).toString());
    }

    @Test
    void testBigDecimalWithString() {
        assertEquals("1.01", (new BigDecimal("1.01")).toString());
    }
}
