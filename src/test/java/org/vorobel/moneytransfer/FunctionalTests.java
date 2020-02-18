package org.vorobel.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.service.MoneyTransferService;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class FunctionalTests {
    private String serverUrl;
    private MoneyTransferService moneyTransferService;

    @BeforeAll
    public void initAll() {
        moneyTransferService = Arc.container().instance(MoneyTransferService.class).get();
        serverUrl = "http://localhost:" + moneyTransferService.getRestPort();
        //moneyTransferService.start();
    }

    @Test
    public void test() {
        HttpResponse<Account> response = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("123456789.01")))
                .asObject(Account.class);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().balance).isEqualTo("123456789.01");

    }

    @AfterAll
    public void tearDownAll() {
        moneyTransferService.stop();
    }

}