package io.helidon.examples.sockshop.users.redis;

import io.helidon.examples.sockshop.users.UserRepository;
import io.helidon.examples.sockshop.users.UserRepositoryTest;

import static io.helidon.examples.sockshop.users.redis.RedisProducers.users;
import static io.helidon.examples.sockshop.users.redis.RedisProducers.client;

/**
 * Tests for Redis repository implementation.
 */
class RedisUserRepositoryIT extends UserRepositoryTest {
    public UserRepository getUserRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","6379"));

        return new RedisUserRepository(users(client(host, port)));
    }
}