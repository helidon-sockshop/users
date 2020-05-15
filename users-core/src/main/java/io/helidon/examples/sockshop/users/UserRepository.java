package io.helidon.examples.sockshop.users;

import java.util.Collection;

/**
 * A repository interface that should be implemented by
 * the various data store integrations.
 */
public interface UserRepository {
    /**
     * Add an {@code Address} to the specified user identifier.
     *
     * @param userID  the user id associated to the address
     * @param address the address to add
     *
     * @return the {@code Address.Id} of the address
     */
    Address.Id addAddress(String userID, Address address);

    /**
     * Return the {@code Address} for the specified identifier.
     *
     * @param id the address id
     *
     * @return the {@code Address} with the specified identifier
     */
        Address getAddress(Address.Id id);

    /**
     * Remove the address with the specified identifier.
     *
     * @param id the address id
     */
    void removeAddress(Address.Id id);

    /**
     * Add a {@code Card} to the specified user identifier.
     *
     * @param userID the user id
     * @param card   the card to add
     *
     * @return the {@code Card.Id} of the card
     */
    Card.Id addCard(String userID, Card card);

    /**
     * Return the {@code Card} with the specified identifier.
     *
     * @param id the card id
     *
     * @return the {@code Card} with the specified identifier
     */
    Card getCard(Card.Id id);

    /**
     * Remove the card  with the specified identifier.
     *
     * @param id the card id
     */
    void removeCard(Card.Id id);

    /**
     * Return all users.
     *
     * @return a collection of {@code User}s
     */
    Collection<? extends User> getAllUsers();

    /**
     * Return an existing {@code User} for the identifier; or a newly created {@code User}.
     *
     * @param id the user identifier
     *
     * @return the {@code User} with the specified user identifier
     */
    User getOrCreate(String id);

    /**
     * Return the {@code User} with the specified user identifier.
     *
     * @param id the user identifier
     *
     * @return the {@code User} with the specified user identifier
     */
     User getUser(String id);

    /**
     * Remove the {@code User} with the specified user identifier;
     *
     * @param id the identifier
     *
     * @return the removed {@code User}; can be {@code null}
     */
    User removeUser(String id);

    /**
     * Authenticate a {@code User} with the specified username against
     * the specified password.
     *
     * @param username the username to be authenticated
     * @param password the password to authenticate against
     *
     * @return true if password match
     */
    boolean authenticate(String username, String password);
        User register(User user);
}
