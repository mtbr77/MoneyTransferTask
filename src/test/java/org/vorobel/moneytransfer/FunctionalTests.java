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
import org.vorobel.moneytransfer.model.Transfer;
import org.vorobel.moneytransfer.service.ConfigurationService;
import org.vorobel.moneytransfer.service.RestService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class FunctionalTests {
    private String serverUrl = "http://localhost:" + ConfigurationService.getRestServicePort();
    //private RestService moneyTransferService;

    /*@BeforeAll
    public void initAll() {
        //restService = Arc.container().instance(RestService.class).get();
        serverUrl = ;
    }*/

    @Test
    public void testTwoAccountsCreation() {
        HttpResponse<Account> response = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("123456780.01")))
                .asObject(Account.class);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().balance).isEqualTo("123456780.01");
        assertThat(response.getBody().id).isEqualTo(1);


    }

    @Test
    public void testGetAccounts() {
        HttpResponse<Account> response = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("9.1")))
                .asObject(Account.class);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().balance).isEqualTo("9.1");
        assertThat(response.getBody().id).isEqualTo(2);

        var response1 = Unirest
                .get(serverUrl + "/accounts").asString();
                //.asObject(Account[].class);
        //assertThat(response.getStatus()).isEqualTo(200);
        System.out.println(response1.getBody());
        //Account[] accounts = response.getBody();
        //assertThat(accounts[0].balance).isEqualTo("123456780.01");
    }

    @Test
    public void testTransferCreation() {
        HttpResponse<Transfer> response = Unirest
                .post(serverUrl + "/transfers")
                .body(JavalinJson.toJson(new Transfer(2,1,"9.1")))
                .asObject(Transfer.class);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().success).isEqualTo(true);
        assertThat(response.getBody().id).isEqualTo(1);

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
        assertThat(response1.getBody().balance).isEqualTo("0");
        assertThat(response1.getBody().id).isEqualTo(2);
    }

    //@AfterAll
    //public void tearDownAll() { restService.stop(); }

}