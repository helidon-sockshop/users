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

    @BeforeEach
    void setup() {
        users.removeUser("testuser");
    }

    @Test
    void testUserCreation() {
        User u = users.getOrCreate("testuser");
        u.setUsername("testuser");
        u.setLastName("test");
        users.register(u);

        assertThat(users.getUser("testuser").getLastName(), is("test"));
        users.removeUser("testuser");
    }

    @Test
    void testAddAddress() {
        User u = users.getOrCreate("testuser");
        u.setUsername("testuser");
        AddressId addrId = u.addAddress(new Address("555", "woodbury St", "Westford", "01886", "USA")).getId();
        users.register(u);
        assertThat(users.getAddress(addrId).getCity(), is("Westford"));
    }

    @Test
    void testAddCard() {
        User u = users.getOrCreate("testuser");
        u.setUsername("testuser");
        CardId cardId = u.addCard(new Card("1234123412341234", "12/19", "123")).getId();
        users.register(u);
        assertThat(users.getCard(cardId).getLongNum(), is("1234123412341234"));
    }

    @Test
    void testUserAuthentication() {
        users.removeUser("testuser");
        User u1 = users.getOrCreate("testuser");
        u1.setUsername("testuser");
        u1.setPassword("pass");
        users.register(u1);

        assertThat(users.authenticate("testuser", "wrong"), is(false));
        assertThat(users.authenticate("testuser", "pass"), is(true));
    }

    @Test
    void testUserDeletion() {
        User u = users.getOrCreate("testuser");
        u.setUsername("testuser");
        users.register(u);

        users.removeUser("testuser");
        assertThat(users.getUser("testuser"), is(nullValue()));
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
