/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
        users.removeUser("foouser");
        users.removeUser("baruser");
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
            body(new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass")).
        when().
            post("/register").
        then().
            statusCode(200).
            body("id", is("baruser"));
    }
}
