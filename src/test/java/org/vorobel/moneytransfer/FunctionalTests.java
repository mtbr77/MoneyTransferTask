package org.vorobel.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.model.Transfer;
import org.vorobel.moneytransfer.service.ConfigurationService;
import org.vorobel.moneytransfer.service.RestService;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionalTests {
    private String serverUrl = "http://localhost:" + ConfigurationService.getRestServicePort();
    private RestService restService;

    @BeforeAll
    public void initAll() {
        restService = new RestService();
        restService.start();
    }

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
    }

    @Test
    @Order(2)
    public void testGetAccounts() {
        HttpResponse<Account[]> response = Unirest.get(serverUrl + "/accounts").asObject(Account[].class);
        assertThat(response.getStatus()).isEqualTo(200);
        Account[] accounts = response.getBody();
        assertThat(accounts[0].balance).isEqualTo("123456780.01");
        assertThat(accounts[1].balance).isEqualTo("9.1");
    }

    @Test
    @Order(3)
    public void testTransferCreation() {
        HttpResponse<Transfer> response1 = Unirest
                .post(serverUrl + "/transfers")
                .body(JavalinJson.toJson(new Transfer(2,1,"9.1")))
                .asObject(Transfer.class);
        assertThat(response1.getStatus()).isEqualTo(200);
        assertThat(response1.getBody().success).isEqualTo(true);
        assertThat(response1.getBody().id).isEqualTo(4);

        HttpResponse<Account> response2 = Unirest.get(serverUrl + "/accounts/1").asObject(Account.class);
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getBody().balance).isEqualTo("123456789.11");
        assertThat(response2.getBody().id).isEqualTo(1);

        HttpResponse<Account> response3 = Unirest.get(serverUrl + "/accounts/2").asObject(Account.class);
        assertThat(response3.getStatus()).isEqualTo(200);
        assertThat(response3.getBody().balance).isEqualTo("0.0");
        assertThat(response3.getBody().id).isEqualTo(2);
    }

    @Test
    @Order(4)
    public void testGetTransfers() {
        HttpResponse<Transfer[]> response = Unirest.get(serverUrl + "/transfers").asObject(Transfer[].class);
        assertThat(response.getStatus()).isEqualTo(200);
        Transfer[] transfers = response.getBody();
        assertThat(transfers[0].amount).isEqualTo("9.1");
        assertThat(transfers[0].success).isTrue();
    }

    @Test
    @Order(5)
    public void testDeleteAll() {
        HttpResponse response1 = Unirest.delete(serverUrl + "/accounts").asEmpty();
        assertThat(response1.getStatus()).isEqualTo(200);

        HttpResponse<Account[]> response2 = Unirest.get(serverUrl + "/accounts").asObject(Account[].class);
        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getBody().length).isEqualTo(0);

        HttpResponse response3 = Unirest.delete(serverUrl + "/transfers").asEmpty();
        assertThat(response3.getStatus()).isEqualTo(200);

        HttpResponse<Transfer[]> response4 = Unirest.get(serverUrl + "/transfers").asObject(Transfer[].class);
        assertThat(response4.getStatus()).isEqualTo(200);
        assertThat(response4.getBody().length).isEqualTo(0);
    }

    @AfterAll
    public void tearDownAll() {
        restService.stop();
        Unirest.shutDown();
    }
}