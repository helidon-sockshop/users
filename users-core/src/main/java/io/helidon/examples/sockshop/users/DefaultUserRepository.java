package io.helidon.examples.sockshop.users;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

/**
 *
 */
@ApplicationScoped
@Traced
public class DefaultUserRepository implements UserRepository {
    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public Address.Id addAddress(String userID, Address address) {
        return users.get(userID).addAddress(address).getId();
    }

    @Override
    public Address getAddress(Address.Id id) {
        return users.get(id.getCustomerId()).getAddress(id);
    }

    @Override
    public void removeAddress(Address.Id id) {
        users.get(id.getCustomerId()).removeAddress(id);
    }

    @Override
    public Card.Id addCard(String userID, Card card) {
        return users.get(userID).addCard(card).getId();
    }

    @Override
    public Card getCard(Card.Id id) {
        return users.get(id.getCustomerId()).getCard(id);
    }

    @Override
    public void removeCard(Card.Id id) {
        users.get(id.getCustomerId()).removeCard(id);
    }

    @Override
    public Collection<? extends User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getOrCreate(String id) {
        return users.getOrDefault(id, new User());
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public User removeUser(String id) {
        return users.remove(id);
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null ? user.authenticate(password) : false;
    }

    @Override
    public User register(User user) {
        return users.putIfAbsent(user.getUsername(), user);
    }

    // --- helpers ----------------------------------------------------------

    @PostConstruct
    public void createTestUsers() {
        User user = new User("Test", "User", "user@weavesocks.com", "user", "pass");
        user.addCard(new Card("1234123412341234", "12/19", "123"));
        user.addAddress(new Address("123", "Main St", "Springfield", "12123", "USA"));
        register(user);

        User aleks = new User("Aleks", "Seovic", "aleks@weavesocks.com", "aleks", "pass");
        aleks.addCard(new Card("4567456745674567", "10/20", "007"));
        aleks.addAddress(new Address("555", "Spruce St", "Tampa", "33633", "USA"));
        register(aleks);

        User bin = new User("Bin", "Chen", "bin@weavesocks.com", "bin", "pass");
        bin.addCard(new Card("3691369136913691", "01/21", "789"));
        bin.addAddress(new Address("123", "Boston St", "Boston", "01555", "USA"));
        register(bin);

        User harvey = new User("Harvey", "Raja", "harvey@weavesocks.com", "harvey", "pass");
        harvey.addCard(new Card("6854657645765476", "03/22", "456"));
        harvey.addAddress(new Address("123", "O'Farrell St", "San Francisco", "99123", "USA"));
        register(harvey);

        User randy = new User("Randy", "Stafford", "randy@weavesocks.com", "randy", "pass");
        randy.addCard(new Card("6543123465437865", "08/23", "042"));
        randy.addAddress(new Address("123", "Mountain St", "Denver", "74765", "USA"));
        register(randy);
    }
}
