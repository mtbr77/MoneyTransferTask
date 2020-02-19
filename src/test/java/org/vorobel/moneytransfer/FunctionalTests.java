package org.vorobel.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.model.Transfer;
import org.vorobel.moneytransfer.service.ConfigurationService;
import org.vorobel.moneytransfer.service.RestService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
public class FunctionalTests {
    private String serverUrl = "http://localhost:" + ConfigurationService.getRestServicePort();

    @Test
    @Order(1)
    public void testTwoAccountsCreation() {
        HttpResponse<Account> response1 = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("123456780.01")))
                .asObject(Account.class);
        assertThat(response1.getStatus()).isEqualTo(201);
        assertThat(response1.getBody().balance).isEqualTo("123456780.01");
        assertThat(response1.getBody().id).isEqualTo(1);

        HttpResponse<Account> response2 = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("9.1")))
                .asObject(Account.class);
        assertThat(response2.getStatus()).isEqualTo(201);
        assertThat(response2.getBody().balance).isEqualTo("9.1");
        assertThat(response2.getBody().id).isEqualTo(2);

        System.out.println("(1)"+Unirest.get(serverUrl + "/accounts").asString().getBody());
    }

    @Test
    @Order(2)
    public void testGetAccounts() {
        HttpResponse<Account[]> response = Unirest
                .get(serverUrl + "/accounts")
                .asObject(Account[].class);
        assertThat(response.getStatus()).isEqualTo(200);
        Account[] accounts = response.getBody();
        assertThat(accounts[0].balance).isEqualTo("123456780.01");
        assertThat(accounts[1].balance).isEqualTo("9.1");
    }

    @Test
    @Order(3)
    public void testTransferCreation() {
        HttpResponse<Transfer> response = Unirest
                .post(serverUrl + "/transfers")
                .body(JavalinJson.toJson(new Transfer(2,1,"9.1")))
                .asObject(Transfer.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().success).isEqualTo(true);
        assertThat(response.getBody().id).isEqualTo(3);

        System.out.println("(2)"+Unirest.get(serverUrl + "/accounts").asString().getBody());

        HttpResponse<Account> response1 = Unirest
                .get(serverUrl + "/accounts/1")
                .asObject(Account.class);
        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getBody().balance).isEqualTo("123456789.11");
        assertThat(response1.getBody().id).isEqualTo(1);

        response1 = Unirest
                .get(serverUrl + "/accounts/2")
                .asObject(Account.class);
        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getBody().balance).isEqualTo("0.0");
        assertThat(response1.getBody().id).isEqualTo(2);
    }

    @Test
    @Order(4)
    public void testGetTransfers() {
        HttpResponse<Transfer[]> response = Unirest
                .get(serverUrl + "/transfers")
                .asObject(Transfer[].class);
        assertThat(response.getStatus()).isEqualTo(200);
        Transfer[] transfers = response.getBody();
        assertThat(transfers[0].amount).isEqualTo("9.1");
        assertThat(transfers[0].success).isTrue();
    }

    @AfterAll
    public void tearDownAll() { Unirest.shutDown(); }

}