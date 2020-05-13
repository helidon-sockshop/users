package io.helidon.examples.sockshop.users;

import io.helidon.microprofile.server.Server;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.users.UserResource}.
 */

public class UserResourceIT {
    private static Server SERVER;
    private UserRepository users;

    /**
     * This will start the application on ephemeral port to avoid port conflicts.
     * We can discover the actual port by calling {@link io.helidon.microprofile.server.Server#port()} method afterwards.
     */
    @BeforeAll
    static void startServer() {
        // disable global tracing so we can start server in multiple test suites
        System.setProperty("tracing.global", "false");
        SERVER = Server.builder().port(0).build().start();
    }

    /**
     * Stop the server, as we cannot have multiple servers started at the same time.
     */
    @AfterAll
    static void stopServer() {
    SERVER.stop();
    }

    @BeforeEach
    void setup() {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = SERVER.port();
        users = getUserRepository();
    }

    protected UserRepository getUserRepository() {
        return SERVER.cdiContainer().select(UserRepository.class).get();
    }

    @Test
    public void testAuthentication() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().auth().preemptive().basic("foouser", "pass").
        when().
            get("/login").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    public void testRegister() {
        users.removeUser("baruser");
        given().
            contentType(JSON).
            body(new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass"), ObjectMapperType.JSONB).
        when().
            post("/register").
        then().
            statusCode(200).
            body("id", is("baruser"));
    }

    @Test
    public void testRegisterAddress() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().
            contentType(JSON).
            body(new AddressesResource.AddAddressRequest("16", "huntington", "lexington", "01886", "us", "foouser")).
        when().
            post("/addresses").
        then().
            statusCode(200).
            body("id", containsString("foouser"));
    }

    @Test
    public void testGetAddress() {
      users.removeUser("foouser");
      User u = new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass");
      Address.Id addrId = u.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
      users.register(u);

      given().
              pathParam("id", addrId.toString()).
      when().
              get("/addresses/{id}").
      then().
            statusCode(OK.getStatusCode()).
            body("number", is("555"),
                    "city", is("Westford"));
    }

    @Test
    public void testDeleteAddress() {
        User u = users.getOrCreate("foouser");
        u.setUsername("foouser");
        Address.Id addrId = u.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
        users.register(u);

        given().
            pathParam("id", addrId.toString()).
        when().
            delete("/addresses/{id}").
        then().
            statusCode(200);
    }

    @Test
    public void testRegisterCard() {
        users.register(new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass"));
        given().
            contentType(JSON).
            body(new CardsResource.AddCardRequest("3691369136913691", "01/21", "789", "foouser")).
        when().
            post("/cards").
        then().
            statusCode(200).
            body("id", containsString("foouser"));
    }

    @Test
    public void testGetCard() {
        User u = users.getOrCreate("cardUser");
        u.setUsername("cardUser");
        Card.Id cardId = u.addCard(new Card("3691369136913691", "01/21", "789")).getId();
        users.register(u);
        given().
            pathParam("id", cardId.toString()).
        when().
            get("/cards/{id}").
        then().
            statusCode(OK.getStatusCode()).
            body("longNum", containsString("3691"),
                    "ccv", is("789"));
    }

    @Test
    public void testDeleteCard() {
        User u = users.getOrCreate("cardUser");
        u.setUsername("cardUser");
        Card.Id cardId = u.addCard(new Card("3691369136913691", "01/21", "789")).getId();
        users.register(u);
        given().
            pathParam("id", cardId.toString()).
        when().
            get("/cards/{id}").
        then().
            statusCode(OK.getStatusCode());
    }

    @Test
    public void testGetAllCards() {
        when().
            get("/cards").
        then().
            statusCode(OK.getStatusCode());
    }
}
