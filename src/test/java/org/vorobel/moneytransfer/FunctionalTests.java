package org.vorobel.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import io.micronaut.context.annotation.Value;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FunctionalTests {
    @Value("${httpserver.port}")
    int port;
    private String serverUrl;
    private MoneyTransferService moneyTransferService;

    @BeforeAll
    public void initAll() {
        moneyTransferService = new MoneyTransferService();
        serverUrl = "http://localhost:" + port;
        moneyTransferService.run();
    }

    @Test
    public void test() {
        HttpResponse<Account> response = Unirest
                .post(serverUrl + "/accounts")
                .body(JavalinJson.toJson(new Account("1")))
                .asObject(Account.class);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getBalance()).isEqualTo("1");

    }

    @AfterAll
    public void tearDownAll() {
        moneyTransferService.stop();
    }

}