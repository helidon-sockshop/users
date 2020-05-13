package io.helidon.examples.sockshop.users;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultUserRepositoryTest extends UserRepositoryTest{
    @Override
    protected UserRepository getUserRepository() {
        return new DefaultUserRepository();
    }
}