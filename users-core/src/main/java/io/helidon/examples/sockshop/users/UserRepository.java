package io.helidon.examples.sockshop.users;

import java.util.Collection;

/**
 */
public interface UserRepository {
    Address.Id addAddress(String userID, Address address);
    Address getAddress(Address.Id id);
    void removeAddress(Address.Id id);

    Card.Id addCard(String userID, Card card);
    Card getCard(Card.Id id);
    void removeCard(Card.Id id);

    Collection<? extends User> getAllUsers();
    User getOrCreate(String id);
    User getUser(String id);
    User removeUser(String id);

    boolean authenticate(String username, String password);
    User register(String id, User user);
}
