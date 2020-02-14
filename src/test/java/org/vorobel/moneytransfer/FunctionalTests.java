package org.vorobel.moneytransfer;

import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FunctionalTests {

    private Application app = new Application();
    private String usersJson = JavalinJson.toJson(UserController.users);

    @BeforeAll
    public  void () {
        app.start();
    }

    @Test
    public void GET_to_fetch_users_returns_list_of_users() {
        app.start(1234);
        HttpResponse response = Unirest.get("http://localhost:1234/users").asString();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(usersJson);

    }

    @AfterAll
    static void tearDownAll() {
        app.stop();
    }

}