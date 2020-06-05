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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.users.AddressesResource}.
 */

public class CustomerResourceIT {
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
        return SERVER.cdiContainer().select(DefaultUserRepository.class).get();
    }

    @Test
    public void testAllCustomers() {
        when().
            get("/customers").
        then().log().all().
            statusCode(200).
                body("size()", is(1));
    }

    @Test
    void testGetCustomer() {
        when().
            get("/customers/{id}", "user").
        then().log().all().
            statusCode(200).
            body("firstName", is("Test"));
    }

    @Test
    void testDeleteCustomer() {
        given().
            pathParam("id", "bin").
        when().
            delete("/customers/{id}").
        then().
            statusCode(200).
            body("status", is(true));
    }

    @Test
    void testGetCustomerCards() {
        when().
            get("/customers/{id}/cards", "aleks").
        then().
            statusCode(200).
            body("size()", is(1));
    }

    @Test
    void testGetCustomerAddresses() {
        when().
            get("/customers/{id}/addresses", "aleks").
        then().
            statusCode(200).
            body("size()", is(1));
    }
}
