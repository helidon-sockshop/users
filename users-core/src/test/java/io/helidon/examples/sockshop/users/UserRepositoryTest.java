package io.helidon.examples.sockshop.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Abstract base class containing tests for all
 * {@link io.helidon.examples.sockshop.users.UserRepository} implementations.
 */
public abstract class UserRepositoryTest {
    private UserRepository users = getUserRepository();

    protected abstract UserRepository getUserRepository();

    @Test
    void testUserCreation() {
        User u = users.getOrCreate("alex");
        u.setUsername("alex");
        u.setLastName("chace");
        users.register(u);

        assertThat(users.getUser("alex").getLastName(), is("chace"));
        users.removeUser("alex");
    }

    @Test
    void testAddAddress() {
        User u1 = users.getOrCreate("alex");
        u1.setUsername("alex");
        Address.Id addrId = u1.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
        users.register(u1);

        assertThat(users.getAddress(addrId).getCity(), is("Westford"));
        users.removeUser("alex");
    }

    @Test
    void testAddCard() {
        User u = users.getOrCreate("alex");
        u.setUsername("alex");
        Card.Id cardId = u.addCard(new Card("1234123412341234", "12/19", "123")).getId();
        users.register(u);

        assertThat(users.getCard(cardId).getLongNum(), is("1234123412341234"));
        users.removeUser("alex");
    }

    @Test
    void testUserAuthentication() {
        users.removeUser("alex");
        User u1 = users.getOrCreate("alex");
        u1.setUsername("alex");
        u1.setPassword("pass");
        users.register(u1);

        assertThat(users.authenticate("alex", "wrong"), is(false));
        assertThat(users.authenticate("alex", "pass"), is(true));

        users.removeUser("alex");
    }

    @Test
    void testUserDeletion() {
        User u1 = users.getOrCreate("adela");
        u1.setUsername("adela");
        users.register(u1);

        users.removeUser("adela");
        assertThat(users.getUser("adela"), is(nullValue()));

    }

    @Test
    void testAllUsers() {
        User u1 = new User("foo", "passfoo", "foo@weavesocks.com", "foouser", "pass");
        User u2 = new User("bar", "passbar", "bar@weavesocks.com", "baruser", "pass");
        User u3 = new User("zar", "passzar", "zar@weavesocks.com", "zaruser", "pass");

        users.register(u1);
        users.register(u2);
        users.register(u3);

        Collection<? extends User>  allUsers = users.getAllUsers();

        assertThat(allUsers.size(), greaterThanOrEqualTo(3));
    }
}
