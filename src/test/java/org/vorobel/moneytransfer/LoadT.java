package org.vorobel.moneytransfer;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.model.Transfer;
import org.vorobel.moneytransfer.service.ConfigurationService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.LongStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoadT {
    private final String serverUrl = "http://localhost:" + ConfigurationService.getRestServicePort();
    private final String initialBalance = ConfigurationService.getInitialBalance();
    private final String transferAmount = ConfigurationService.getTransferAmount();
    private final long expectedAccounts = ConfigurationService.getAccountsNumber();
    private final long expectedTransfersAmount = ConfigurationService.getTransfersNumber();
    private final String accountPayload = "{\"balance\":" + initialBalance + "}";
    private final long initialId = 4;
    private AtomicLong accountsCounter;
    private BigDecimal expectedTotalBalance;
    private Random random = new Random();

    @AllArgsConstructor
    class PostAccountTask implements Runnable {
        private AtomicLong accountsCounter;
        @Override
        public void run() {
            var response = Unirest.post(serverUrl + "/accounts").body(accountPayload).asObject(Account.class);

            if(response.getStatus() == 201)
                accountsCounter.incrementAndGet();
        }
    }

    @AllArgsConstructor
    class PostTransferTask implements Runnable {
        private AtomicLong failedTransfersCounter;
        @Override
        public void run() {
            long n = accountsCounter.longValue();
            long source = Math.abs(random.nextLong()) % n + 1 + initialId;
            long destination = Math.abs(random.nextLong()) % n + 1 + initialId;
            if (destination == source) {
                destination = (source - initialId + 1) % n + 1 + initialId;
            }

            String payload = "{\"source\":" + source + ","+ "\"destination\":" + destination + ","+ "\"amount\":" + transferAmount + "}";


            var response = Unirest.post(serverUrl + "/transfers").body(payload).asObject(Transfer.class);

            int status = response.getStatus();
            boolean success = response.getBody().success;

            if(status != 200 || !success)
                failedTransfersCounter.incrementAndGet();
        }
    }

    private BigDecimal getTotalBalance() {
        HttpResponse<Account[]> response = Unirest.get(serverUrl + "/accounts").asObject(Account[].class);

        if(response.getStatus() == 200) {
            Account[] accounts = response.getBody();
            return new BigDecimal(Account.getTotalBalance(accounts));
        }

        return BigDecimal.ZERO;
    }
/*
    @Test
    @Order(1)
    public void AccountsCreationTest() throws InterruptedException {
        accountsCounter = new AtomicLong(0);

        ExecutorService pool = Executors.newCachedThreadPool();
        rangeClosed(1, expectedAccounts).parallel()
                .forEach(i -> pool.submit(new PostAccountTask(accountsCounter)));

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        expectedTotalBalance = new BigDecimal(initialBalance).multiply(new BigDecimal(accountsCounter.intValue()));

        assertThat(accountsCounter.intValue()).isEqualTo(expectedAccounts);
    }

    @Test
    @Order(2)
    public void TransfersCreationTest() throws InterruptedException {
        AtomicLong failedTransfersCounter = new AtomicLong(0);

        ExecutorService pool = Executors.newCachedThreadPool();

        rangeClosed(1, expectedTransfersAmount).parallel()
                .forEach(i -> pool.submit(new PostTransferTask(failedTransfersCounter)));

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("% of failed transfers = " + (failedTransfersCounter.intValue()*100/expectedTransfersAmount));

        assertThat(getTotalBalance().compareTo(expectedTotalBalance)).isEqualTo(0);
    }*/
}
